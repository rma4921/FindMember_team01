function openEditModal() {
    document.getElementById('editModal').style.display = 'block';
    document.getElementById("modalOverlay").style.display = "block";
}

function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    document.getElementById("modalOverlay").style.display = "none";
}

function confirmDelete() {
    return confirm('정말 삭제하시겠습니까?');
}

function openApplyModal() {
    document.getElementById('applyModal').style.display = 'block';
    document.getElementById("modalOverlay").style.display = "block";
}

function closeApplyModal() {
    document.getElementById('applyModal').style.display = 'none';
    document.getElementById("modalOverlay").style.display = "none";
}

function openReportModal() {
    document.getElementById('reportModal').style.display = 'block';
    document.getElementById("modalOverlay").style.display = "block";
}

function closeReportModal() {
    document.getElementById('reportModal').style.display = 'none';
    document.getElementById("modalOverlay").style.display = "none";
}

function changeApplyButton() {
    const button = document.getElementById('applyButton');
    button.disabled = true;
    button.innerText = '지원 완료';
    button.style.backgroundColor = '#ccc';
    button.style.cursor = 'default';
    return true;
}