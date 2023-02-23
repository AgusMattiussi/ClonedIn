import React from 'react';
import './App.css';
import Header from './header';
import Login from './login';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Jobs from './jobs'


// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css";
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element= {
            <div className="home">
              <Header/>
              <Login/>
            </div>
          } />
          <Route path="/jobs" element= {
            <Jobs/>
          } />
        </Routes>
      </BrowserRouter>  
    </div>
  );
}

export default App;
