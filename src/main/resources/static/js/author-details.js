const loadBooksForm = document.getElementById("loadBooks");
const authorIdField = document.getElementById("authorId");
const booksSection = document.getElementById("booksSection");

function getBookOverview(book) {
    return `<div class="offer card col-sm-6 col-md-3  col-lg-2 m-1 p-0">
                <div class="card-img-top-wrapper">
                    <img class="card-img-top" src="${book.picture}" alt="Book image">
                </div>
                <ul class="offer-details list-group list-group-flush">
                    <li class="list-group-item">
                        <div class="card-text"><span>${book.title} </span></div>
                        <div class="card-text"><span>• Genre: ${book.genre}</span></div>
                    </li>
                </ul>
                <div class="card-body">
                    <a class="card-link" href="/books/${book.id}/details">Details</a>
                </div>
            </div>`
}

async function loadBooksByAuthor(event) {
    event.preventDefault();
    const authorId = authorIdField.value;

    fetch(`http://localhost:8080/api/books?authorId=${authorId}`)
        .then(res => res.json())
        .then(data => {
            if(booksSection.childElementCount === 0) {
                for (const book of data) {
                    booksSection.innerHTML += getBookOverview(book);
                }
            }

            if (data.length == 0) {
                booksSection.innerHTML = `<h2 class="text-center text-white mt-5">No books of this author available</h2>`
            }
        });
}

loadBooksForm.addEventListener('submit', loadBooksByAuthor);