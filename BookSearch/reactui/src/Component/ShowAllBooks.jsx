import React,{Component} from 'react';
import BookService from '../Service/BookService';

class ShowAllBooks extends Component{
    constructor(props){
        super(props);
        this.state = {
            books: [],
            name:''
        }
        this.counter = 0;
        this.handleKeyDown = this.handleKeyDown.bind(this);
        this.shorten = this.shorten.bind(this);
        // this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        BookService.uploadAllBooks("Computer Science").then(
            response => {
                console.log(response);
                this.setState({books:response.data});
            }
        );
    }

    loadBooks(name){
    //first step get all books from backend
        BookService.uploadAllBooks(name).then(
            response => {
                console.log(response)
                this.setState({books:response.data});
            }
        );
    }

    handleKeyDown(event){
        if(event.key == "Enter"){
            console.log("Enter was pressed...");
            const target = event.target;
            const name = target.value;
            this.loadBooks(name);
        }
    }

    handleChange(event){
        const target = event.target;
        const value = target.value; //assign new value 
    
        var name = value;
        return name;
    }

    selectedBook(isbn){
        console.log('in selected book');
        this.props.history.push(`/books/${isbn}`);
    }

    shorten(str, maxLen, separator = ' ') {
        if (str.length <= maxLen) return str;
        var shortenString =  str.substr(0, str.lastIndexOf(separator, maxLen));
        return(
            <div>
                <p className="card-text">Descripion: {shortenString}...</p>
            </div>
        )
      }

    render(){
    console.log('rendering...');
    let booksArray = this.state.books.map(book => {
        let parsedIsbn = book.isbn.split(",");
        return (
            <div className="col-sm-4">
                  <div className="card">
                     <img className="card-img-top" src={book.thumbnail} alt="Card image cap"/>
                     <div class="card-body">
                         <h5 class="card-title">Title: {book.title}</h5>
                         {this.shorten(book.description,100)}
                         <p class="card-text">Author: {book.authors[0]}</p>
                         <a onClick={() => this.selectedBook(parsedIsbn[0])} className="btn btn-primary">View Book</a>
                     </div>
                 </div>
            </div>
        )
    })
    console.log(booksArray);
    return(
            <div className="container">
                <div className="md-form active-cyan-2 mb-3">
                    <input class="form-control" type="text" placeholder="Search" aria-label="Search"  onKeyDown={this.handleKeyDown}/>
                </div>
                <div className="row">
                    {booksArray}
                </div> 
                
            </div>
       
        );
    }   
}

export default ShowAllBooks;