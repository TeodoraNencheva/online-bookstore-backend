const minusBtn = document.getElementById('minusBtn');
const plusBtn = document.getElementById('plusBtn');
const quantityInputField = document.getElementById('quantity');
const buyBookForm = document.getElementById('buyBookForm');
const bookIdInputField = document.getElementById('bookId');
const buyBookSection = document.getElementById('buyBookSection');
const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

async function buyBook(event) {
    event.preventDefault();

    const requestBody = {
        bookId: bookIdInputField.value,
        quantity: quantityInputField.value
    };

    function getConfirmMessage(data) {
        const p = document.createElement('p');
        p.textContent = `${data.quantity} x ${data.title} by ${data.authorFullName} added to cart`;
        return p;
    }

    fetch('http://localhost:8080/api/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify(requestBody)
    }).then(res => res.json())
        .then(data => {
            buyBookSection.appendChild(getConfirmMessage(data));
        });
}

buyBookForm.addEventListener('submit', buyBook);

function decreaseQuantity() {
    let newValue = Number(Number(quantityInputField.value) - 1);
    if (newValue <= 0) {
        newValue = 1;
    }

    quantityInputField.value = newValue;
}

minusBtn.addEventListener('click', decreaseQuantity);

function increaseQuantity() {
    quantityInputField.value = Number(Number(quantityInputField.value) + 1);
}

plusBtn.addEventListener('click', increaseQuantity);