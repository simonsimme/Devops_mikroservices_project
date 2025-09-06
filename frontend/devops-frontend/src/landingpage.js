import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './landingpage.css';

function getDaysInMonth(year, month) {
  const date = new Date(year, month, 1);
  const days = [];
  while (date.getMonth() === month) {
    days.push(new Date(date));
    date.setDate(date.getDate() + 1);
  }
  return days;
}

function getCalendarMatrix(year, month) {
  const days = getDaysInMonth(year, month);
  const firstDay = days[0].getDay();
  const matrix = [];
  let week = new Array(firstDay).fill(null);

  days.forEach(day => {
    if (week.length === 7) {
      matrix.push(week);
      week = [];
    }
    week.push(day);
  });
  while (week.length < 7) week.push(null);
  matrix.push(week);
  return matrix;
}

function Schedule() {
  const location = useLocation();
  const { token, user_id } = location.state || {};
  const [theme, setTheme] = useState('light');
  const [worker, setWorker] = useState(null);
  const [shifts, setShifts] = useState([]);
  const [unassignedShifts, setUnassignedShifts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [workerForm, setWorkerForm] = useState({ name: '', role: '' });
  const [creatingWorker, setCreatingWorker] = useState(false);
  const [assignPopup, setAssignPopup] = useState({ open: false, shift: null });
  const [assignedShifts, setAssignedShifts] = useState([]);

  // Add state for selected month/year
  const now = new Date();
  const [selectedYear, setSelectedYear] = useState(now.getFullYear());
  const [selectedMonth, setSelectedMonth] = useState(now.getMonth());

  // Calendar matrix for selected month/year
  const calendarMatrix = getCalendarMatrix(selectedYear, selectedMonth);

  // Month and year options
  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];
  const yearOptions = [];
  for (let y = now.getFullYear() - 5; y <= now.getFullYear() + 5; y++) {
    yearOptions.push(y);
  }

  const handleThemeToggle = () => {
    setTheme(theme === 'light' ? 'dark' : 'light');
  };
  const handleMonthChange = e => setSelectedMonth(Number(e.target.value));
  const handleYearChange = e => setSelectedYear(Number(e.target.value));

  // Month navigation buttons
  const handlePrevMonth = () => {
    if (selectedMonth === 0) {
      setSelectedMonth(11);
      setSelectedYear(selectedYear - 1);
    } else {
      setSelectedMonth(selectedMonth - 1);
    }
  };
  const handleNextMonth = () => {
    if (selectedMonth === 11) {
      setSelectedMonth(0);
      setSelectedYear(selectedYear + 1);
    } else {
      setSelectedMonth(selectedMonth + 1);
    }
  };

  const handleAssignShift = async () => {
  if (!assignPopup.shift || !worker) return;
  try {
    const response = await fetch('http://localhost:8080/api/shift-assignments/assign', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'X-User-Id': user_id
      },
      body: JSON.stringify({
        shiftId: assignPopup.shift.id,
        workerId: worker.id 
      })
    });
    if (response.ok) {
      setAssignPopup({ open: false, shift: null });
      setLoading(true);
      const shiftsRes = await fetch('http://localhost:8080/api/shifts', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
          'X-User-Id': user_id
        }
      });
      const shiftsData = await shiftsRes.json();
      setShifts(Array.isArray(shiftsData) ? shiftsData : []);
      const assignedRes = await fetch(`http://localhost:8080/api/shift-assignments/worker/${worker.id}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const assignedData = await assignedRes.json();
      setAssignedShifts(Array.isArray(assignedData) ? assignedData : []);
      const unassignedRes = await fetch('http://localhost:8080/api/shifts/unassigned', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const unassignedData = await unassignedRes.json();
      setUnassignedShifts(Array.isArray(unassignedData) ? unassignedData : []);
      setLoading(false);
    } else {
      const errorText = await response.text();
  console.error('Assign shift failed:', response.status, errorText);
  alert('Failed to assign shift.\n' + errorText);
    }
  } catch {
    alert('Error assigning shift.');
  }
};

  useEffect(() => {
    async function fetchWorker() {
      const workerResponse = await fetch(`http://localhost:8080/api/scheduler/me`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
          'X-User-Id': user_id
        }
      });
      if (workerResponse.ok) {
        const workerData = await workerResponse.json();
        setWorker(workerData);
      }
    }
    fetchWorker();
  }, [token, user_id]);

  useEffect(() => {
    async function fetchAssignedShifts() {
      if (!worker) return;
      setLoading(true);
      try {
        const response = await fetch(`http://localhost:8080/api/shift-assignments/worker/${worker.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        const data = await response.json();
        setAssignedShifts(Array.isArray(data) ? data : []);
      } catch {
        setAssignedShifts([]);
      }
      setLoading(false);
    }
    fetchAssignedShifts();
  }, [worker, token]);

  useEffect(() => {
    async function fetchUnassignedShifts() {
      setLoading(true);
      try {
        const response = await fetch('http://localhost:8080/api/shifts/unassigned', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        const data = await response.json();
        setUnassignedShifts(Array.isArray(data) ? data : []);
      } catch (err) {
        setUnassignedShifts([]);
      }
      setLoading(false);
    }
    fetchUnassignedShifts();
  }, [token]);

  const handleWorkerFormChange = e => {
    setWorkerForm({ ...workerForm, [e.target.name]: e.target.value });
  };

  const handleWorkerFormSubmit = async e => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/scheduler', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
          'X-User-Id': user_id
        },
        body: JSON.stringify(workerForm)
      });
      if (response.ok) {
        setCreatingWorker(!creatingWorker);
      } else {
        const errorMsg = await response.text();
        alert('Failed to create worker info: ' + errorMsg);
      }
    } catch {
      alert('Error creating worker info');
    }
  };

  // Map assigned shifts by day for calendar rendering
  const assignedShiftsByDay = {};
  assignedShifts.forEach(assignment => {
    // If assignment.shift exists, use assignment.shift.date, else assignment.date
    const shiftDate = assignment.date
      ? new Date(assignment.date).toISOString().slice(0, 10)
      : assignment.shift && assignment.shift.date
        ? new Date(assignment.shift.date).toISOString().slice(0, 10)
        : null;
    if (shiftDate) {
      if (!assignedShiftsByDay[shiftDate]) assignedShiftsByDay[shiftDate] = [];
      assignedShiftsByDay[shiftDate].push(assignment);
    }
  });

  const today = new Date();

  const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  return (
    
    <div className={`landing-container ${theme}-theme`} style={{ minHeight: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      <div className="landing-box" style={{
        maxWidth: '900px',
        margin: '2rem auto',
        background: theme === 'light' ? '#f9f9fb' : '#23272f',
        borderRadius: '16px',
        boxShadow: '0 4px 24px rgba(49,130,206,0.12)',
        padding: '2rem',
        textAlign: 'center'
      }}>
        <button className="theme-toggle-btn" onClick={handleThemeToggle} style={{
          position: 'absolute',
          top: '2rem',
          right: '2rem',
          border: 'none',
          background: 'transparent',
          fontSize: '1.2rem',
          cursor: 'pointer'
        }}>
          {theme === 'light' ? '🌙 Dark Mode' : '☀️ Light Mode'}
        </button>
        <div>
          <img src={require('./sniperverse.png')} alt="Company Logo" className="company-logo" />
        </div>
        <h2 className="landing-title" style={{ fontWeight: 700, fontSize: '2rem', marginBottom: '1rem' }}>Your Schedule</h2>
        {!worker ? (
          <form onSubmit={handleWorkerFormSubmit} style={{ marginBottom: '1rem', textAlign: 'left', maxWidth: '400px', margin: '0 auto' }}>
            <h3 style={{ fontWeight: 600 }}>Please fill out your worker info:</h3>
            <div style={{ marginBottom: '0.5rem' }}>
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={workerForm.name}
                onChange={handleWorkerFormChange}
                required
                style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid #ccc', marginTop: '4px' }}
              />
            </div>
            <div style={{ marginBottom: '0.5rem' }}>
              <label>Role:</label>
              <select
                name="role"
                value={workerForm.role}
                onChange={handleWorkerFormChange}
                required
                style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid #ccc', marginTop: '4px' }}
              >
                <option value="">Select role</option>
                <option value="floor">Floor</option>
                <option value="floor-manager">Floor Manager</option>
                <option value="administration">Administration</option>
                <option value="manager">Manager</option>
              </select>
            </div>
            <button type="submit" style={{
              background: '#3182ce',
              color: '#fff',
              border: 'none',
              borderRadius: '6px',
              padding: '8px 16px',
              fontWeight: 600,
              cursor: 'pointer',
              marginTop: '8px'
            }}>Save</button>
          </form>
        ) : (
          <div style={{ marginBottom: '1rem', textAlign: 'center' }}>
            <strong>Name:</strong> {worker.name}<br />
            <strong>Role:</strong> {worker.role}
          </div>
        )}
        <p className="landing-welcome" style={{ fontWeight: 600, fontSize: '1.2rem', marginTop: '2rem' }}>Monthly Schedule</p>
        <hr className="landing-divider" style={{ margin: '1rem 0' }} />
        {/* Advanced month/year selector with navigation */}
        <div style={{
          display: 'flex',
          gap: '1rem',
          alignItems: 'center',
          justifyContent: 'center',
          marginBottom: '1.5rem'
        }}>
          <button onClick={handlePrevMonth} style={{
            background: '#3182ce',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            padding: '6px 12px',
            fontWeight: 600,
            cursor: 'pointer'
          }}>
            ◀
          </button>
          <label>
            <select value={selectedMonth} onChange={handleMonthChange} style={{
              padding: '6px 12px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              fontWeight: 600
            }}>
              {monthNames.map((name, idx) => (
                <option key={name} value={idx}>{name}</option>
              ))}
            </select>
          </label>
          <label>
            <select value={selectedYear} onChange={handleYearChange} style={{
              padding: '6px 12px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              fontWeight: 600
            }}>
              {yearOptions.map(y => (
                <option key={y} value={y}>{y}</option>
              ))}
            </select>
          </label>
          <button onClick={handleNextMonth} style={{
            background: '#3182ce',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            padding: '6px 12px',
            fontWeight: 600,
            cursor: 'pointer'
          }}>
            ▶
          </button>
        </div>
        <div style={{ width: '100%', overflowX: 'auto', marginBottom: '2rem', display: 'flex', justifyContent: 'center' }}>
          <table className="calendar-table" style={{
            width: '100%',
            maxWidth: '800px',
            borderCollapse: 'collapse',
            background: theme === 'light' ? '#fff' : '#23272f',
            boxShadow: '0 2px 8px rgba(49,130,206,0.08)',
            borderRadius: '10px',
            overflow: 'hidden',
            margin: '0 auto'
          }}>
            <thead>
              <tr>
                {weekdays.map(weekday => (
                  <th key={weekday} style={{
                    background: theme === 'light' ? '#f7fafc' : '#414345',
                    color: theme === 'light' ? '#4a5568' : '#f5f7fa',
                    padding: '12px',
                    borderBottom: '1px solid #e2e8f0',
                    fontWeight: 700,
                    fontSize: '1rem'
                  }}>
                    {weekday}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {calendarMatrix.map((week, i) => (
                <tr key={i}>
                  {week.map((day, j) => {
                    const dayKey = day ? day.toISOString().slice(0, 10) : null;
                    const assignedForDay = dayKey ? assignedShiftsByDay[dayKey] || [] : [];
                    const isToday = day &&
                      day.getDate() === today.getDate() &&
                      day.getMonth() === today.getMonth() &&
                      day.getFullYear() === today.getFullYear();
                    return (
                      <td key={j} style={{
                        border: '1px solid #e2e8f0',
                        padding: '12px 8px',
                        minWidth: '100px',
                        height: '90px',
                        verticalAlign: 'top',
                        background: isToday
                          ? (theme === 'light' ? '#bee3f8' : '#2a4365')
                          : (theme === 'light' ? '#fff' : '#23272f'),
                        position: 'relative'
                      }}>
                        {day ? (
                          <div>
                            <div style={{
                              fontWeight: 700,
                              color: isToday ? '#2b6cb0' : '#718096',
                              marginBottom: '6px',
                              fontSize: '1.1em'
                            }}>
                              {day.getDate()}
                            </div>
                            {assignedForDay.length > 0 ? (
                              assignedForDay.map(assignment => (
                                <div key={assignment.id} style={{
                                  background: theme === 'light' ? '#c6f6d5' : '#22543d',
                                  borderRadius: '6px',
                                  padding: '4px 8px',
                                  marginBottom: '4px',
                                  fontSize: '0.98em',
                                  boxShadow: '0 1px 4px rgba(49,130,206,0.08)'
                                }}>
                                  <strong>{assignment.requiredRole}</strong><br />
                                  {assignment.startTime} - {assignment.endTime}
                                </div>
                              ))
                            ) : (
                              <span style={{ color: '#bbb', fontSize: '0.95em' }}>No shift</span>
                            )}
                          </div>
                        ) : null}
                      </td>
                    );
                  })}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="landing-welcome" style={{ fontWeight: 600, fontSize: '1.2rem', marginTop: '2rem' }}>Available Shifts</p>
        <hr className="landing-divider" style={{ margin: '1rem 0' }} />
        <div style={{ width: '100%', maxWidth: '800px', margin: '0 auto' }}>
          {loading ? (
            <p>Loading shifts...</p>
          ) : unassignedShifts.length === 0 ? (
            <p>No available shifts.</p>
          ) : (
            <ul style={{ listStyle: 'none', padding: 0 }}>
              {worker && unassignedShifts
                .filter(shift => shift.requiredRole === worker.role)
                .map(shift => (
                  <li
                    key={shift.id}
                    style={{
                      background: theme === 'light' ? '#f7fafc' : '#414345',
                      color: theme === 'light' ? '#4a5568' : '#f5f7fa',
                      marginBottom: '1rem',
                      padding: '1rem',
                      borderRadius: '10px',
                      boxShadow: '0 2px 8px rgba(49,130,206,0.08)',
                      cursor: 'pointer',
                      transition: 'box-shadow 0.2s',
                      border: '2px solid transparent'
                    }}
                    onClick={() => setAssignPopup({ open: true, shift })}
                    onMouseEnter={e => e.currentTarget.style.boxShadow = '0 4px 16px rgba(49,130,206,0.18)'}
                    onMouseLeave={e => e.currentTarget.style.boxShadow = '0 2px 8px rgba(49,130,206,0.08)'}
                  >
                    <strong>Date:</strong> {shift.date}<br />
                    <strong>Role:</strong> {shift.requiredRole}<br />
                    <strong>Start:</strong> {shift.startTime}<br />
                    <strong>End:</strong> {shift.endTime}
                  </li>
                ))}
            </ul>
          )}
        </div>
        {assignPopup.open && assignPopup.shift && (
          <div style={{
            position: 'fixed',
            top: 0, left: 0, right: 0, bottom: 0,
            background: 'rgba(0,0,0,0.3)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 9999
          }}>
            <div style={{
              background: theme === 'light' ? '#fff' : '#23272f',
              padding: '2rem',
              borderRadius: '16px',
              boxShadow: '0 8px 32px rgba(49,130,206,0.18)',
              minWidth: '320px',
              textAlign: 'center'
            }}>
              <h3 style={{ marginBottom: '1rem' }}>Assign Shift</h3>
              <p>
                <strong>Date:</strong> {assignPopup.shift.date}<br />
                <strong>Role:</strong> {assignPopup.shift.requiredRole}<br />
                <strong>Start:</strong> {assignPopup.shift.startTime}<br />
                <strong>End:</strong> {assignPopup.shift.endTime}
              </p>
              <p style={{ margin: '1rem 0' }}>
                Do you want to assign this shift to yourself?
              </p>
              <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
                <button
                  onClick={handleAssignShift}
                  style={{
                    background: '#3182ce',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '6px',
                    padding: '8px 16px',
                    fontWeight: 600,
                    cursor: 'pointer'
                  }}
                >
                  Yes, Assign
                </button>
                <button
                  onClick={() => setAssignPopup({ open: false, shift: null })}
                  style={{
                    background: '#e2e8f0',
                    color: '#2d3748',
                    border: 'none',
                    borderRadius: '6px',
                    padding: '8px 16px',
                    fontWeight: 600,
                    cursor: 'pointer'
                  }}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}
        <div className="landing-footer" style={{ marginTop: '2rem', textAlign: 'center', color: '#718096' }}>
          <span className="landing-contact">Questions? <a href="mailto:support@company.com" style={{ color: '#3182ce', textDecoration: 'underline' }}>Contact support</a></span>
        </div>
      </div>
    </div>
  );
}

export default Schedule;