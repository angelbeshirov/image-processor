import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators'

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

    BASE_PATH: string = 'http://localhost:8080';
    USERNAME_SESSION_TOKEN: string = 'token';

    public username: string;
    public password: string;

    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.get(this.BASE_PATH + 'api/auth/login',
        {
            headers: { authorization: this.createBasicAuthToken(username, password) }
        }).pipe(map(
            res => {
                this.username = username;
                this.password = password;
                this.registerSuccessfulLogin(username);
            }
        ))
    }

    createBasicAuthToken(username: string, password: string) {
        return 'Basic ' + btoa(username + ':' + password);
    }

    registerSuccessfulLogin(username) {
        sessionStorage.setItem(this.USERNAME_SESSION_TOKEN, username);
    }

    logout() {
        sessionStorage.removeItem(this.USERNAME_SESSION_TOKEN);
        this.username = null;
        this.password = null;
    }

    isUserLoggedIn() {
        return !sessionStorage.getItem(this.USERNAME_SESSION_TOKEN) === null;
    }

    getLoggedInUsername() {
        let user = sessionStorage.getItem(this.USERNAME_SESSION_TOKEN);
        return user === null ? '' : user;
    }
}
