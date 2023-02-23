import React from 'react';
import './App.css';
import Header from './header';
import Login from './login';
import { BrowserRouter } from 'react-router-dom';
import Navigation from './navbar'
// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css";
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min";

function App() {
  return (
    <div className="App">
      {/* <Header/>
      <BrowserRouter>
        <Login/>
      </BrowserRouter> */}
      <Navigation/>
    </div>
  );
}

export default App;
