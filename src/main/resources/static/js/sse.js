let userId = document.getElementById("myName").innerText;

console.log(userId);

if (userId.length > 0) {
    const eventSource = new EventSource("/sse" + "?userId=" + userId)
    eventSource.addEventListener("alarm", function (event) {
        let message = event.data;
        Swal.fire({
            // toast:true,
            position: 'top-end',
            icon: 'success',
            title: message,
            showConfirmButton: false,
            timer: 1500
        });
        let alarmMark = document.getElementById("alarmMark");
        alarmMark.style.display = 'block';
    });


    eventSource.addEventListener("liveMatch", function (event) {
        let message = event.data;
        let split = message.split(" ");
        setTimeout(function() {
            Swal.fire({
                icon: 'success',
                title: "실시간 매칭이 성사되었습니다! 채팅방으로 이동해주세요^^",
                showConfirmButton: false,
            })
                .then(function () {
                    //date 채팅ID " " 크루ID
                    console.log('roomId = ' + split[0] + 'crewId = ' + split[1]);
                    location.href = "http://ec2-52-79-236-51.ap-northeast-2.compute.amazonaws.com:8080/view/v1/room?roomId=" + split[0] + "&crewId=" + split[1];
                    // location.href="http://localhost:8080/view/v1/room?roomId="+split[0]+"&crewId=" + split[1];
                });
        }, 2000);
    });


    eventSource.addEventListener("liveMatchCnt", function (event) {
        let message = event.data;
        $("#randomCnt").empty();
        $("#randomCnt").append('현재 대기중인 인원 :' + message);
    });

    eventSource.addEventListener("liveMatchCancel", function (event) {
        $("#randomCnt").empty();
    });
}


