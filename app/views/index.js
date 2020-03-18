import $ from "jquery";
import "materialize-css";
import Dropzone from "dropzone";
import React from "react"
import ReactDOm from "react-dom";

// Materializedの設定
$(
    function () {
        $(".button-collapse").sideNav();
    }
);  // end of document ready