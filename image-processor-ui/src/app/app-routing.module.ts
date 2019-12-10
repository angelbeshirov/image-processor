import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { UploadComponent } from './images/upload/upload.component';
import { SignInComponent } from './auth/sign-in/sign-in.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';
import { ViewAllComponent } from './images/view-all/view-all.component';
import { EditComponent } from './images/edit/edit.component';


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'image', children: [
    { path: '', redirectTo: 'view', pathMatch: 'full' },
    { path: 'view', component: ViewAllComponent },
    { path: 'upload', component: UploadComponent },
    { path: 'edit/:imageId', component: EditComponent }
  ] },
  { path: 'auth', children: [
    { path: '', redirectTo: 'sign-in', pathMatch: 'full' },
    { path: 'sign-in', component: SignInComponent },
    { path: 'sign-up', component: SignUpComponent },
  ] },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
