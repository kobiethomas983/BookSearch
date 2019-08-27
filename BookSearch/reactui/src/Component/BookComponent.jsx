import React, { Component } from 'react';
import BookService from '../Service/BookService';

class BookComponent extends Component{
    constructor(props){
        super(props);
        this.state ={
            //this should match the isbn
            id: this.props.match.params.isbn,
           book: {}
        }
    }

    componentDidMount(){
        this.loadBook();
    }

    loadBook(){
        console.log("in loadbook...");
        //check if the id exist if not just return else find path
        if(this.state.id == -1)
            return;

        BookService.uploadBook(this.state.id).then(
            response => {
                console.log(response);
                this.setState({
                    book: response.data
                });
            }
        );
    }

    editBook(id){ 
        this.props.history.push(`/books/${id}/edit`);
    }

    deleteBook(id){
        
        BookService.deleteBook(id)
        .then( () => this.props.history.push(`/books`));
        ;
    }
    render(){
      
        return(
            <div class="card">
                <img class="card-img-top" src={this.state.book.thumbnail} alt="Card image cap"/>
                <div class="card-body">
                    <h5 class="card-title"><strong>Title:</strong> {this.state.book.title}</h5>
                    <p class="card-text"><strong>Rating:</strong> {this.state.book.averageRating}</p>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><strong>Author:</strong>{this.state.book.authors}</li>
                    <li class="list-group-item"><strong>Category:</strong>{this.state.book.categories}</li>
                    <li class="list-group-item"><strong>Pages:</strong> {this.state.book.pageCount}</li>
                </ul>
                <div class="card-body">
                    <a href="#" class="card-link">Go back</a>
                </div>
          </div>
           
        );
    }
}

export default BookComponent;