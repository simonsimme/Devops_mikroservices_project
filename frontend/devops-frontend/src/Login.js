

import './Login.css';
import React, { useState } from 'react';
import Schedule from './landingpage.js';
import { useNavigate } from 'react-router-dom';


function Login() {
    const [theme, setTheme] = useState('light');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleThemeToggle = () => {
        setTheme(theme === 'light' ? 'dark' : 'light');
    };

    const handleSubmit = async (e) =>{
        e.preventDefault();
  const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await response.json();

    if (response.ok) {
      // Successful login
      alert('Login successful!');
      const token = data.token;
      const user_id = data.userId;
      
      navigate('/landing', { state: { token, user_id} });

      // Save token, redirect, etc.
    } else {
      // Show error
      alert(data.message || 'Login failed');
    }
    };

  return (
    <div className={`login-container ${theme}-theme`}>
      <div className="login-box">
        <button className="theme-toggle-btn" onClick={handleThemeToggle}>
          {theme === 'light' ? '🌙 Dark Mode' : '☀️ Light Mode'}
        </button>
        <div className="logo-bg">
          <img src={require('./sniperverse.png')} alt="Company Logo" className="company-logo" />
        </div>
        <h2 className="login-title">COMPANY</h2>
        <p className="login-welcome">Sign in to access your schedule</p>
        <hr className="login-divider" />
        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter your email"
              required
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              placeholder="Enter your password"
              required
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
          </div>
          <button type="submit" className="login-btn">Login</button>
        </form>
        <div className="login-footer">
          <span className="login-contact">Need help? <a href="mailto:support@company.com">Contact support</a></span>
        </div>
      </div>
    </div>
  );
}

export default Login;