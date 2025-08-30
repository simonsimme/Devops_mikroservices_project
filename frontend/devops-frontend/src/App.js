import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login.js';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />        {/* Main start page */}
      </Routes>
    </Router>
  );
}

export default App;