import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../auth/authentication.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  projectTitle: string = 'Image Processor';
  loggedUser: string = "";

  constructor(private auth: AuthenticationService) {
  }

  ngOnInit() {
  }

  logout() {
    this.auth.logout();
  }

  authenticated() {
    return this.auth.isUserLoggedIn();
  }

}
