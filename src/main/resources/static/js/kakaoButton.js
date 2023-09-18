let loginButton = document.querySelector("#login");
let logoutButton = document.querySelector('#logout');

loginButton.addEventListener('click', () => {
    window.location = 'https://kauth.kakao.com/oauth/authorize' +
        '?client_id=109b90382c698be04116088d022f0acb' +
        '&redirect_uri=http://localhost:8080/kakaoOauth' +
        '&response_type=code'
})

logoutButton.addEventListener('click', () => {
    window.location = 'https://kauth.kakao.com/oauth/logout?client_id=109b90382c698be04116088d022f0acb&logout_redirect_uri=http://localhost:8080/'
})