// 버튼 및 모달 요소 가져오기
const mapButton = document.getElementById("mapButton");
const modal = document.getElementById("modal");
const closeBtn = document.getElementsByClassName("close")[0];

// '지도' 버튼 클릭 시 모달 열기
mapButton.addEventListener("click", function() {
    modal.style.display = "block";
});

// 모달 닫기 버튼 클릭 시 모달 닫기
closeBtn.addEventListener("click", function() {
    modal.style.display = "none";
});

// 모달 외 다른 부분 클릭 시 모달 닫기
window.addEventListener("click", function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
});