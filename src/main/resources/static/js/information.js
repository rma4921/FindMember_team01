document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', e => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
        });
    });

    const sortButtons = document.querySelectorAll('.sort-btn');
    sortButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            sortButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });

    const openModal = document.getElementById('openModal');
    const closeModal = document.getElementById('closeModal');
    const modalOverlay = document.getElementById('modalOverlay');

    if (openModal && modalOverlay && closeModal) {
        openModal.addEventListener('click', () => {
            modalOverlay.classList.remove('hidden');
        });
        closeModal.addEventListener('click', () => {
            modalOverlay.classList.add('hidden');
        });
    }
});

function checkWriter(el, writerId, loggedInId) {
    if (String(writerId) !== String(loggedInId)) {
        alert("작성자가 아닙니다.");
        return false;
    }
    return true;
}

function showNotAuthorAlert() {
    alert("작성자가 아닙니다.");
    return false;
}
