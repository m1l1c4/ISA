/*!

=========================================================
* Paper Kit React - v1.0.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-kit-react

* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/paper-kit-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter, Route, Redirect, Switch } from "react-router-dom";

// styles
import "assets/css/bootstrap.css";
import "assets/scss/paper-kit.scss";
import "assets/demo/demo.css";
// pages
import Index from "views/Index.js";
import NucleoIcons from "views/NucleoIcons.js";
import RegisterPage from "views/examples/RegisterPage.js";
import ClinicPage from "views/examples/ClinicPage.js";
import MedicalWorkerPage from "views/examples/MedicalWorkerPage.js";
import RegistrationRequest from "views/examples/RegistrationRequest.js";
import AdministratorPage from "views/examples/AdministratorPage.js";
import PatientPage from "views/examples/PatientPage.js";
import ClinicHomePage from "views/examples/ClinicHomePage.js";
import ActivateAccount from "views/examples/ActivateAccount";
import ConfirmCheckup from "views/examples/ConfirmCheckup";
import CodebookPage from "views/examples/CodebookPage.js";
import ViewAndEditClinic from "views/examples/ViewAndEditClinic";
import MyCalendar from "./views/examples/Calendar";
import MapGoogle from "./views/examples/MapGoogle";
import Checkup from "views/examples/Checkup";
// others

ReactDOM.render(
  <BrowserRouter>
    <Switch>
      <Route path="/index" render={props => <Index {...props} />} />
      <Route
        path="/nucleo-icons"
        render={props => <NucleoIcons {...props} />}
      />
      <Route
        path="/register-page"
        render={props => <RegisterPage {...props} />}
      />
      <Route
        path="/clinic-page"
        render={props => <ClinicPage {...props} />}
      />
      <Route
        path="/medicalworker-page"
        render={props => <MedicalWorkerPage {...props} />}
      />
      <Route
        path="/registration-request"
        render={props => <RegistrationRequest {...props} />}
      />
      <Route
        path="/administrator-page"
        render={props => <AdministratorPage {...props} />}
      />
      <Route
        path="/patient-page/:id"
        render={props => <PatientPage {...props} />}
      />
      <Route
        path="/codebook-page"
        render={props => <CodebookPage {...props} />}
      />
      <Route
        path="/clinic-homepage/:id"
        render={props => <ClinicHomePage {...props} />}
      />
      <Route
        path="/ActivateAccount/:id"
        render={props => <ActivateAccount {...props} />}
      />
      <Route
        path="/ConfirmCheckup/:id"
        render={props => <ConfirmCheckup {...props} />}
      />
      <Route
        path="/Checkup/:id"
        render={props => <Checkup {...props} />}
      />
      <Route
        path="/viewandeditclinic-page"
        render={props => <ViewAndEditClinic {...props} />}
      />
      <Route
        path="/calendar"
        render={props => <MyCalendar {...props} />}
      />
      <Route
        path="/map"
        render={props => <MapGoogle {...props} />}
      />
      <Redirect to="/register-page" />
    </Switch>
  </BrowserRouter>,
  document.getElementById("root")
);
