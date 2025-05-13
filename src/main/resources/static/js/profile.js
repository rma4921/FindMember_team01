const userId = document.body.dataset.userId;

function submitForm() {
    const payload = {
        name: document.getElementById("name").value,
        nickname: document.getElementById("nickname").value,
        email: document.getElementById("email").value,
        techStack: document.getElementById("techStack").value
    };

}
