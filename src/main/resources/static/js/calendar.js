document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'timeGridWeek', // Vizualizare pe săptămână cu ore
        selectable: true,            // Permite utilizatorului să facă click și drag
        selectMirror: true,          // Afișează un eveniment "fantomă" în timp ce trage cu mouse-ul

        // Acest cod se execută INSTANT când utilizatorul a terminat de selectat intervalul
        select: function(selectionInfo) {
            // selectionInfo conține automat data și ora de start și end selectate
            console.log("Start:", selectionInfo.startStr);
            console.log("End:", selectionInfo.endStr);

            // 1. Deschide fereastra modală (ex: folosind Bootstrap Modal)
            $('#reservationModal').modal('show');

            // 2. Pre-completează automat câmpurile ascunse sau vizibile din formularul tău
            document.getElementById('modalStartTime').value = selectionInfo.startStr.substring(0, 16);
            document.getElementById('modalEndTime').value = selectionInfo.endStr.substring(0, 16);

            // 3. Curăță selecția albastră de pe calendar după ce s-a deschis modala
            calendar.unselect();
        },

        // Endpoint-ul tău din Spring Boot creat anterior
        events: '/api/events'
    });

    calendar.render();
});