import "./styles/App.css"
import Login from "./views/login"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import DiscoverJobs from "./views/discoverJobs";
import RegisterUser from './views/registerUser';
import RegisterEnterprise from './views/registerEnterprise';

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/discoverJobs" element={<DiscoverJobs />} />
          <Route path="/registerUser" element= {
              <RegisterUser/>
            } />
          <Route path="/registerEnterprise" element= {
            <RegisterEnterprise/>
          } />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
