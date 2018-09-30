var Utils = (function() {
    return {
        confirmDelete: function(msg, callback) {
            bootbox.confirm({
                message: msg,
                buttons: {
                    confirm: {
                        label: 'Yes',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'No',
                        className: 'btn-danger'
                    }
                },
                callback: callback
            });
        }
    };
})();