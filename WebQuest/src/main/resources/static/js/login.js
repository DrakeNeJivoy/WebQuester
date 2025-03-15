document.getElementById("loginButton").addEventListener("click", function() {
    let formData = new FormData(document.getElementById("loginForm"));

    fetch("/login", {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url; // Перенаправление при успешном входе
            } else {
                return response.text();
            }
        })
        .then(data => {
            document.getElementById("message").innerHTML = data; // Вывод ошибки
        })
        .catch(error => console.error("Ошибка:", error));
});
