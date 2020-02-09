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
import React , {Component} from "react";
import axios from 'axios';
import "assets/css/bootstrap.min.css";
// reactstrap components
import { Button, Alert, Form, FormGroup, Container, Col, Row} from "reactstrap";
import { withRouter } from "react-router-dom";
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
// core components
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';

class ConfirmCheckup extends Component {
  constructor(props)
  {
    super(props);
    this.state = {
      showResponse: false ,      
      registerShow: false,      
      idCheckup: "",
      message: "" ,
      date: "" ,
      time: "" ,
      doctor: "" ,
      specialization: "" ,
      clinic: ""
    };

    this.activate = this.activate.bind(this) ;
    this.notactivate = this.notactivate.bind(this) ;
    
  }
      
  doc = document.documentElement.classList.remove("nav-open");
  /*eff = useEffect(() => {
    document.body.classList.add("register-page");
    return function cleanup() {
      document.body.classList.remove("register-page");
    };
  });*/
  componentDidMount()
  {
    const {id} = this.props.match.params ;
    this.setState({idCheckup:id});
    this.getCheckup();
  }
  
  redirect = () => {
    this.props.history.push('/register-page');
  }

  getCheckup = () => {
      let p = this.state.idCheckup ;
    axios({
        method: 'get',
        url: 'http://localhost:8099/checkup/' + p ,      
      }).then((response)=>{      
          if (response.status === 200) {
              this.setState({date: response.data.date[2]+"/"+response.data.date[1]+"/"+response.data.date[0], 
                time: response.data.time, specialization: response.data.type , 
                doctor: response.data.medicalWorker.user.name + " " + response.data.medicalWorker.user.surname,
                clinic: response.data.clinic.name});
          }
             
        
      },(error)=>{
        console.log(error);
      });
  }


  activate = () => {
    let p = this.state.idCheckup ;
    axios({
      method: 'post',
      url: 'http://localhost:8099/checkup/scheduleCheckup/' + p ,      
    }).then((response)=>{      
        if (response.status === 200) {
          NotificationManager.success('Vas pregled je uspjesno zakazan!', 'Uspjesno!', 3000);
            this.setState({message: "Vaš pregled je uspešno zakazan. Uskoro ćete biti preusmereni na početnu stranicu.", showResponse: true})
            //const redirect = this.redirect ;
            let timerREdirection = setTimeout( this.redirect, 5000);
        }
              
      
    },(error)=>{
      console.log(error);
    });
  }  

  notactivate = () => {
    let p = this.state.idCheckup ;
    axios({
      method: 'post',
      url: 'http://localhost:8099/checkup/cancelCheckup/' + p ,      
    }).then((response)=>{      
        if (response.status === 200) {
          NotificationManager.success('Uspjesno ste otkazali pregled!', 'Uspjesno!', 3000);
            this.setState({message: "Otkazali ste pregled. Uskoro ćete biti preusmereni na početnu stranicu.", showResponse: true})
            let timerREdirection = setTimeout( this.redirect, 5000);
        }
              
      
    },(error)=>{
      console.log(error);
    });
  }
  
  
  render() {
    return (
    <div>
          
      <ProfilePageHeader />
      <Alert color="info" isOpen={this.state.showResponse} toggle={() => this.setState({showResponse:false})}>
            <b>{this.state.message}</b> 
      </Alert> 
      <div className="section profile-content">
        <Container >
          <div className="owner">            
            <div className="name">
              <h4 className="title">
                KONFIRMACIJA PREGLEDA<br />
              </h4>              
            </div>
          </div>          
          <br />
          <Col className="ml-auto mr-auto" md="6"> 
        <Form> 
        <FormGroup>
          <label>Da li želite da potvrdite zakazivanje pregleda?</label>        
        </FormGroup>   
        <Row>     
        <Col><Button block className="btn-round" color="info" onClick={this.activate}>
            DA
        </Button></Col>
        <Col><Button block className="btn-round" color="info"  onClick={this.notactivate} >
            NE
        </Button></Col>
        </Row>
        </Form>                       
        </Col>
        <br></br>
        <div className="row">  
             <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Datum: {this.state.date}</label>                        
                      </div>
                    </div>           
         </div>  
         <div className="row">                           
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Vreme: {this.state.time + "h"}</label>                        
                      </div>
                </div> 
        </div> 
        <div className="row">  
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Vrsta pregleda: {this.state.specialization}</label>                        
                      </div>
                </div> 
                </div> 
                <div className="row">  
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Doktor: {this.state.doctor}</label>                        
                      </div>
                </div> 
                </div> 
                <div className="row">  
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Klinika: {this.state.clinic}</label>                        
                      </div>
                </div> 
                    </div>
        </Container>
      </div>
      <NotificationContainer/>
    </div>
    )};
}

export default withRouter(ConfirmCheckup);
