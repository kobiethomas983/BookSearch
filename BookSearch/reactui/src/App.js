import React, {Component} from 'react';
import logo from './logo.svg';
import BooksApp from './Component/BooksApp';
import './App.css';

class App extends Component {
  render(){
    return (
      <div className="container">
        <BooksApp />
      </div>
    );
  }
}

export default App;
