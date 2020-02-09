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
import { Button, Form, FormGroup, Container, Col, Row} from "reactstrap";
import { withRouter } from "react-router-dom";
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
// core components
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';

class ActivateAccount extends Component {
  constructor(props)
  {
    super(props);
    this.state = {
      showResponse: false ,      
      registerShow: false,      
      idUser: "",
      message: ""
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
    this.setState({idUser: id});
  }
  
  redirect = () => {
    this.props.history.push('/register-page');
  }

  activate = () => {
    let p = this.state.idUser ;
    axios({
      method: 'post',
      url: 'http://localhost:8099/activateEmail/' + p ,      
    }).then((response)=>{      
        if (response.status === 200)
          this.redirect();      
      
    },(error)=>{
      console.log(error);
    });
  }  

  notactivate = () => {
    NotificationManager.info('Odbili ste zahtjev za registraciju', 'Info!', 3000);
  }
  
  
  render() {
    return (
    <div>
          
      <ProfilePageHeader />
      <div className="section profile-content">
        <Container >
          <div className="owner">            
            <div className="name">
              <h4 className="title">
                AKTIVACIJA NALOGA<br />
              </h4>              
            </div>
          </div>          
          <br />
          <Col className="ml-auto mr-auto" md="6"> 
        <Form> 
        <FormGroup>
          <label>Da li želite da aktivirate svoj nalog?</label>        
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
        </Container>
      </div>
 
      <NotificationContainer/>
    </div>
    )};
}

export default withRouter(ActivateAccount);
