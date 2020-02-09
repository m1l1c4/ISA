import React, {Component} from 'react';
import {Map, InfoWindow, Marker, GoogleApiWrapper} from 'google-maps-react';
 
export class MapGoogle extends Component{
    state = {
        name: this.props.name,
        address:  this.props.address,
        city:  this.props.city,
        state: this.props.state,
        lat: 0,
        lng: 0,
        showingInfoWindow: false,
        activeMarker: {},
    };
 
    componentDidUpdate(prevProps, prevState){
        if(prevProps.address !== this.state.address || prevProps.city !== this.state.city  || prevProps.state !== this.state.state){
            this.setState({address: this.props.address, city:this.props.city, state: this.props.state}, () =>{
                this.handleMap();
            });
        }
    }
 
    componentDidMount(){
        this.handleMap();
    }
 
    stringParser = (path) => {
        console.log('path', path)
        var words = path.split(' ')
        var ret = words[0]
        var i
        for(i=1; i<words.length; i++){
            ret += '+' + words[i]
        }
        return ret;
    }
 
    handleMap = () =>{
        fetch('https://api.opencagedata.com/geocode/v1/json?q='
        +this.stringParser(this.state.address) + '%2c' + this.stringParser(this.state.city)
        + '%2c' + this.state.state + '&key=9a621993023f424796cb415f13d07beb').then(response => response.json())
 
        .then(data => {
            console.log('pravi poziv', data)
            if(data.status.code !== 200){
                this.setState({
                    address: 'nije dostupna',
                    state: 'nije dostupno',
                    city: 'nije dostupno'
                })
            }else{
                this.setState({
                    lat: data.results[0].geometry.lat,
                    lng: data.results[0].geometry.lng
                })
            }
        })
    }
 
    onMarkerClick = (props, marker, e) => {
        this.setState({
            activaMarker: marker,
            showingInfoWindow: true
        });
    }
   
 
    render() {
        const mapStyles = {
            position: 'static',
            margin: 'auto',
            width: '20%',
            height: '20%'
        };
 
        if(this.state.lat === 0 &&  this.state.lng === 0){
            return( <div>
                <h2> Mapa trenutno nije dostupna </h2>
            </div>);
        }
        //onClick = {this.onMarkerClick}
        else{
            return (
                <Map google = {this.props.google} zoom = {6} syle = {mapStyles} initialCenter ={{lat: this.state.lat , lng: this.state.lng}} >
                <Marker position = {{ lat: this.state.lat, lng:this.state.lng}} name = {'Trenutna lokacija'}> </Marker>
                <InfoWindow
                marker = {this.state.activeMarker}
                visible = {this.state.showingInfoWindow}>
                    <div class="container">
                        <div class="section-title text-center">
                            <div>
                                <p > Drzava: {this.state.state} </p>
                                <p > Grad: {this.state.city} </p>
                                <p > Adresa: {this.state.address} </p>
                                <p > </p>
                            </div>
                        </div>
                    </div>
                </InfoWindow>
                <div class="container">
                  <div class="section-title text-center">
                      <div>
                         
                      </div>
                  </div>
              </div>
                </Map>
                 
                );
 
        }
    }
}
 
 
export default GoogleApiWrapper({
    apiKey: 'AIzaSyA0aqRI4uzz_2cLTkurwCYLUNKzw81FOvU',
    language: 'ENG'
})(MapGoogle);