import React, { } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Homepage from './views/pages/Homepage';
import './App.css';
import './assets/css/style.css';

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route path='/' exact component={Homepage} />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
