const userId = document.body.dataset.userId;

function submitForm() {
    const payload = {
        name: document.getElementById("name").value,
        nickname: document.getElementById("nickname").value,
        email: document.getElementById("email").value,
        techStack: document.getElementById("techStack").value
    };

}

function confirmDelete(id) {
    if (confirm("정말 탈퇴하시겠습니까?")) {
        fetch(`/api/user/${id}`, {
            method: "DELETE"
        })
        .then(() => {
            alert("탈퇴 완료");
            window.location.href = "/api/posts";
        });
    }
}