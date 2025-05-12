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

function checkRole(event) {
  const role = document.getElementById('role')?.value;
  const allowed = ['MASTER', 'ADMIN'];

  console.log("memberRole value:", role);

  if (!allowed.includes(role)) {
    alert('MASTER 이상 등급만 댓글을 작성할 수 있습니다.');
    return false; // 제출 방지
  }
    return true; // 제출 허용
}

