import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  credentials = {username: '', password: ''};
  successMessage: string;
  errorMessage: string = 'Invalid Credentials';
  invalidLogin: boolean = false;
  loginSuccess: boolean = false;

  constructor(private auth: AuthenticationService, private router: Router) { }

  ngOnInit() {
  }

  login() {
    this.auth.login(this.credentials.username, this.credentials.password).subscribe(
      result => {
        this.invalidLogin = false;
        this.loginSuccess = true;
        this.successMessage = "Login successful.";
        this.router.navigate(['/home']);
      }, (
        result => {
          this.invalidLogin = true;
          this.loginSuccess = false;
        }
      )
    );
  }

}
