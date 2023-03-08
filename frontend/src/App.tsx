import React from "react"
import "./App.css"
import Login from "./login"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import DiscoverJobs from "./discoverJobs";
import RegisterUser from './registerUser';
import RegisterEnterprise from './registerEnterprise';

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/discoverJobs" element={<DiscoverJobs />} />
          <Route path="/registerUser" element= {
              <RegisterUser/>
            } />
          <Route path="/registerUser" element= {
            <RegisterEnterprise/>
          } />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
