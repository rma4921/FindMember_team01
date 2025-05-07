// script.js - 공통 동작 스크립트 (탭, 정렬, 모달 등)

document.addEventListener('DOMContentLoaded', () => {
  // 탭 전환
  const tabs = document.querySelectorAll('.tab');
  tabs.forEach(tab => {
    tab.addEventListener('click', e => {
      e.preventDefault();
      tabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
    });
  });

  // 정렬 버튼
  const sortButtons = document.querySelectorAll('.sort-btn');
  sortButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      sortButtons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
    });
  });

  // 글쓰기 모달 열기
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
