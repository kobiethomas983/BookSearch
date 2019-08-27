import axios from 'axios';

const URL = "http://localhost:8080";
const books = "books";
const BooksAPIURL = `${URL}/${books}`;
class BookService{
    
    uploadAllBooks(name) {
        console.log(BooksAPIURL);
       return axios.get(`http://localhost:8080/api/v1/googlebooks/bookname/${name}`);
    }
    uploadBook(isbn){
        console.log('in upload book method...');
        return axios.get(`http://localhost:8080/api/v1/googlebooks/bookname/showbook/${isbn}`);
    }
    createBook(newBook){
        console.log('in create book...');
        return axios.post("http://localhost:8080/books", newBook);
    }
    updateBook(editBook,id){
        console.log("Updating book....");
        return axios.put(`http://localhost:8080/books/${id}`, editBook);
    }
    deleteBook(id){
        console.log("delete book...");
        return axios.delete(`http://localhost:8080/books/${id}`);
    }
}

export default new BookService();