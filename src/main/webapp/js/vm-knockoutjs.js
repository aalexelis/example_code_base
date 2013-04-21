function Line(line) {
    //Basic data
    this.product = ko.observable("");
    this.unitprice_su = ko.observable("");
    this.qnty = ko.observable("");

    //Callback function holders
    this.guid = ko.observable(line.guid);
    this.cbproduct = ko.observable(line.cbproduct);
    this.cbunitprice_su = ko.observable(line.cbunitprice_su);
    this.cbqnty = ko.observable(line.cbqnty);

    //Viewmodel binde
    this.product.subscribe(function(newValue){
        this.value = newValue
        eval(this.cbproduct());
    }, this);
    this.unitprice_su.subscribe(function(newValue){
        this.value = newValue
        eval(this.cbunitprice_su());
    }, this);
    this.qnty.subscribe(function(newValue){
        this.value = newValue
        eval(this.cbqnty());
    }, this);

    //UI helper data, only client side
    this.amount = ko.computed({
        read: function() {
        if (this.unitprice_su() && this.qnty()) {return this.unitprice_su() * this.qnty();}
        },
        owner: this
    });


}

function ViewModel() {
    var self = this;

    // Data
    self.lines = ko.observableArray([]);
    self.cbsubmit = ko.observable();
    self.submit = function(){
        eval(self.cbsubmit());
    }

    //Behaviour
    self.addLine = function() {
        $.ajax({
              dataType: "json",
              url: "/bind-newline",
              data: {
                guid: "",
                product: "",
                unitprice_su: "",
                qnty: ""
              },
              success: function( data ) {
                  self.lines.push( new Line({
                    guid: data.guid,
                    cbproduct: data.product,
                    cbunitprice_su: data.unitprice_su,
                    cbqnty: data.qnty
                  }));
              }
        });
    }
    self.setSubmit = function() {
        $.ajax({
                  dataType: "json",
                  url: "/bind-submit",
                  data: {
                    submit: ""
                  },
                  success: function( data ) {
                      self.cbsubmit(data.cbsubmit);
                  }
            });
    }




    //Initialization
    self.addLine();
    self.setSubmit();

}

// Activates knockout.js
ko.applyBindings(new ViewModel());