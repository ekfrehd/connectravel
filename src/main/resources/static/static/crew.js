document.getElementById("crew_btn").addEventListener('click',crewMake);
document.getElementById("crew_btn_d").addEventListener('click',deleteCrew);
document.getElementById("crew_btn_m").addEventListener('click',modifyCrew);
document.getElementById("crew_btn_f").addEventListener('click',findCrew);
document.getElementById("crew_btn_fa").addEventListener('click',findAllCrew);
document.getElementById("crew-btn-detail").addEventListener('click',detailCrew);

async function crewMake() {
    let response = await fetch("/api/v1/crews", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
        body: JSON.stringify({
            content: document.getElementById("content").value,
            title: document.getElementById("title").value
        })
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        document.getElementById("area_crew").innerHTML = json.result.crewId + " 모임생성 " + json.result.message;
    }else{
        let json = await response.text();
        document.getElementById("area_crew").innerHTML = json;
    }
}

async function deleteCrew() {
    let response = await fetch("/api/v1/crews/" + +document.getElementById("crewId").value, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
        body: JSON.stringify({
        })
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        document.getElementById("area_crew").innerHTML = json.result.crewId + " 삭제 " + json.result.message;
    }else{
        let json = await response.text();
        document.getElementById("area_crew").innerHTML = json;
    }
}

async function deleteCrewManager() {
    let response = await fetch("/api/v1/crews/" + +document.getElementById("crewId").value, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
        body: JSON.stringify({
        })
    })
    if(response.ok){
        let json = await response.json();

        confirm("모임이 삭제 되었습니다.");
        window.location.href = '/view/v1/manage/crews';
    }else{
        let json = await response.text();
        alert(json);
    }
}

async function modifyCrew() {
    let response = await fetch("/api/v1/crews/" + +document.getElementById("crewId").value, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
        body: JSON.stringify({
            title: "modified title",
            content: "modified content",
            strict: "modified strict"
        })
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        document.getElementById("area_crew").innerHTML = json.result.crewId + " 수정 " + json.result.message;
    }else{
        let json = await response.text();
        document.getElementById("area_crew").innerHTML = json;
    }
}

async function findCrew() {
    let response = await fetch("/api/v1/crews/" + +document.getElementById("crewId").value, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        document.getElementById("area_crew").innerHTML = "getTitle: " + json.result.title + " getContent: " + json.result.content;
    }else{
        let json = await response.text();
        document.getElementById("area_crew").innerHTML = json;
    }
}

async function findAllCrew() {
    let response = await fetch("/api/v1/crews/", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem('jwt'),
            "Authentication": localStorage.getItem('jwt')
        },
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        for(var i = 0;i<json.result.totalElements;i++){
            document.getElementById("area_crew").innerHTML += json.result.content[i].title + "\n";
        }
        document.getElementById("area_crew").innerHTML += json.result.totalElements;

    }else{
        let json = await response.text();
        document.getElementById("area_crew").innerHTML = json;
    }
}

async function detailCrew() {
    let response = await fetch("/api/v1/crews/" + +document.getElementById("crewDetailId").value, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
    if(response.ok){
        let json = await response.json()
        console.log(json);
        document.getElementById("title").innerHTML += json.result.title ;
        // document.getElementById("area_crew").innerHTML += json.result.strict ;
        document.getElementById("content").innerHTML += json.result.content ;
        // document.getElementById("area_crew").innerHTML += json.result.crewLimit ;

    }else{
        let json = await response.text();
        document.getElementById("area_detail").innerHTML = json;
    }
}