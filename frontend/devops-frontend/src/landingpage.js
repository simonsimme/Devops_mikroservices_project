import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './landingpage.css';

function Schedule() {
  const location = useLocation();
  const { token, user_id } = location.state || {};
  const [theme, setTheme] = useState('light');
  const [worker, setWorker] = useState(null);
  const [shifts, setShifts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [workerForm, setWorkerForm] = useState({ name: '', role: '' });
  const [creatingWorker, setCreatingWorker] = useState(false);

  const handleThemeToggle = () => {
    setTheme(theme === 'light' ? 'dark' : 'light');
  };

  useEffect(() => {
    async function fetchWorker() {
      try {
        const response = await fetch('http://localhost:8080/api/scheduler/me', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
            'X-User-Id': user_id 
          }
        });
        if (response.ok) {
          const data = await response.json();
          setWorker(data);
          
        } else {
          setWorker(null);
          alert('Failed to fetch worker info');
        }
      } catch (err) {
        setWorker(null);
      }
    }
    fetchWorker();
  }, [token, creatingWorker]);

  useEffect(() => {
    async function fetchShifts() {
      setLoading(true);
      try {
        const response = await fetch('http://localhost:8080/api/shifts', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        const data = await response.json();
        setShifts(Array.isArray(data) ? data : []); // <-- Ensure it's always an array
      } catch (err) {
        setShifts([]);
      }
      setLoading(false);
    }
    fetchShifts();
  }, [token]);

  const handleWorkerFormChange = e => {
    setWorkerForm({ ...workerForm, [e.target.name]: e.target.value });
  };

  const handleWorkerFormSubmit = async e => {
    e.preventDefault();
    alert(user_id)
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
        setCreatingWorker(!creatingWorker); // trigger refetch
      } else {
        const errorMsg = await response.text();
        alert('Failed to create worker info: ' + errorMsg);
      }
    } catch {
      alert('Error creating worker info');
    }
  };

  return (
    <div className={`landing-container ${theme}-theme`}>
      <div className="landing-box">
        <button className="theme-toggle-btn" onClick={handleThemeToggle}>
          {theme === 'light' ? '🌙 Dark Mode' : '☀️ Light Mode'}
        </button>
        <div className="logo-bg">
          <img src={require('./sniperverse.png')} alt="Company Logo" className="company-logo" />
        </div>
        <h2 className="landing-title">Your Schedule</h2>
        {!worker ? (
          <form onSubmit={handleWorkerFormSubmit} style={{ marginBottom: '1rem', textAlign: 'left' }}>
            <h3>Please fill out your worker info:</h3>
            <div>
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={workerForm.name}
                onChange={handleWorkerFormChange}
                required
              />
            </div>
            <div>
              <label>Role:</label>
              <select
              name="role"
              value={workerForm.role}
              onChange={handleWorkerFormChange}
              required
            >
              <option value="">Select role</option>
              <option value="floor">Floor</option>
              <option value="floor-manager">Floor Manager</option>
              <option value="administration">Administration</option>
              <option value="manager">Manager</option>
            </select>
            </div>
            <button type="submit">Save</button>
          </form>
        ) : (
          <div style={{ marginBottom: '1rem', textAlign: 'left' }}>
            <strong>Name:</strong> {worker.name}<br />
            <strong>Role:</strong> {worker.role}
          </div>
        )}
        <p className="landing-welcome">Upcoming Shifts</p>
        <hr className="landing-divider" />
        <div style={{ width: '100%' }}>
          {loading ? (
            <p>Loading shifts...</p>
          ) : shifts.length === 0 ? (
            <p>No shifts assigned.</p>
          ) : (
            <ul style={{ listStyle: 'none', padding: 0 }}>
              {shifts.map(shift => (
                <li key={shift.id} style={{
                  background: theme === 'light' ? '#f7fafc' : '#414345',
                  color: theme === 'light' ? '#4a5568' : '#f5f7fa',
                  marginBottom: '1rem',
                  padding: '1rem',
                  borderRadius: '10px',
                  boxShadow: '0 2px 8px rgba(49,130,206,0.08)'
                }}>
                  <strong>Date:</strong> {shift.date}<br />
                  <strong>Role:</strong> {shift.requiredRole}<br />
                  <strong>Start:</strong> {shift.startTime}<br />
                  <strong>End:</strong> {shift.endTime}
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="landing-footer">
          <span className="landing-contact">Questions? <a href="mailto:support@company.com">Contact support</a></span>
        </div>
      </div>
    </div>
  );
}

export default Schedule;