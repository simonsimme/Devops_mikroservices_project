import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login.js';
import Schedule from './landingpage.js';

function App() {
  return (
    <Router>
      <Routes>

        <Route path="/" element={<Login />} />        {/* Main start page */}
        <Route path="/landing" element={<Schedule />} /> {/* Landing page after login */}

      </Routes>
    </Router>
  );
}

export default App;