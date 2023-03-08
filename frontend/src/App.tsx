import React from "react"
import "./App.css"
import Login from "./login"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import DiscoverJobs from "./discoverJobs"

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
          <Route path="/home" element={<DiscoverJobs />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
