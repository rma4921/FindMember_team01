const userId = document.body.dataset.userId;

function submitForm() {
    const payload = {
        name: document.getElementById("name").value,
        nickname: document.getElementById("nickname").value,
        email: document.getElementById("email").value,
        techStack: document.getElementById("techStack").value
    };

}

function onImageButtonClick(level) {
    if (level < 5 && level != 0) {
        alert('레벨 5부터 대표 이미지를 설정할 수 있습니다.');
    } else {
        document.getElementById('imageUpload').click();
    }
}