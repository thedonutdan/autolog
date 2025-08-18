import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import VehiclesPage from './pages/VehiclesPage'
import VehicleDetailsPage from './pages/VehicleDetailsPage'
import LoginPage from './pages/LoginPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/vehicles" element={<VehiclesPage />} />
        <Route path="/vehicles/:id" element={<VehicleDetailsPage />} />
        {/* Other routes will live here later*/}
      </Routes>
    </Router>
  );
}

export default App
