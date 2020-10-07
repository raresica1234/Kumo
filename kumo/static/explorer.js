let request = new XMLHttpRequest()

request.open('GET', document.URL + "explore", true)

request.onload = function() {
    let data = JSON.parse(this.response)

    let content = ""

    for (let i = 0; i < data.length; i++) {
        content += "<h1> " + data[i][0] + ", Type: " + (data[i][1] ? "directory" : "file") + "</h1>"
    }

    document.getElementById("explorer").innerHTML = content

    console.log(data)
}

request.send()