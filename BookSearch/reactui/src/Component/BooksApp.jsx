import React, {Component} from 'react';
import listedBooks from './ShowAllBooks';
import listBook from './BookComponent';
import {BrowserRouter as Router,Switch,Route} from 'react-router-dom';

class BooksApp extends Component{
    render(){
        return (
            <Router>
                <>
                <h1 className='app-header'>Books Application</h1>
                <Switch>
                    <Route path='/' exact component={listedBooks}/>
                    <Route path='/books' exact component={listedBooks}/>
                    <Route path='/books/:isbn' exact component={listBook}/>
                </Switch>
                </>
                
            </Router>
            
            

        );
    }
}

export default BooksApp;