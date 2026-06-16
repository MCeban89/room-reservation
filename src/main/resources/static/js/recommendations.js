function updateEndPreview() {
    const startVal = document.getElementById('startHour').value;
    const durationVal = document.getElementById('duration').value;
    const preview = document.getElementById('endPreview');
    const endInput = document.getElementById('endHour');

    if (!startVal || !durationVal) {
        preview.innerHTML = 'Selectează ora start și durata';
        endInput.value = '';
        return;
    }

    const [hours, minutes] = startVal.split(':').map(Number);
    const startMinutes = hours * 60 + minutes;
    const endMinutes = startMinutes + parseInt(durationVal);

    if (endMinutes > 18 * 60) {
        preview.innerHTML = '<span style="color:var(--danger)">Depășește ora 18:00</span>';
        endInput.value = '';
        return;
    }

    const endHours = Math.floor(endMinutes / 60);
    const endMins = endMinutes % 60;
    const endStr = String(endHours).padStart(2, '0') + ':' + String(endMins).padStart(2, '0');

    preview.innerHTML = 'Se termină la <span>' + endStr + '</span>';
    endInput.value = endStr;
}

function validateForm() {
    const endHour = document.getElementById('endHour').value;
    if (!endHour) {
        alert('Selectează ora start și durata rezervării.');
        return false;
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function () {
    if (document.getElementById('startHour').value &&
        document.getElementById('duration').value) {
        updateEndPreview();
    }
});