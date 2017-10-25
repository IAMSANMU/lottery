
$.extend(true, $.fn.dataTable.defaults, {
    serverSide: true,
    lengthChange: false,
    searching: false,
    select: 'single',
    info: false,
    autoWidth: false,
    processing: true
});
$.extend({
	preArrData:function(data,searchModel){
		$.each( searchModel, function (key, obj) {
			$.each(obj,function(name,val){
				data["searchModel["+key+"]."+name]=val;
			});
		} );
	}
})


$.fn.extend({
    preDataTableEvent: function (option) {
        var instance = null;
        var searchModel = null;
        var addTosearchModel = function (elm) {
            var searchColum, searchOperator, searchValue;
            var $elm = $(elm);
            searchValue = $elm.val();
            if (searchValue) {
                searchColum = $elm.attr('searchColum');
                searchOperator = $elm.attr('searchOperator');
                searchModel.push({
                    operator: searchOperator,
                    value: searchValue,
                    column: searchColum
                });
            }
        }
        var self = $(this);

        var startSearch = $('#search-content [startSearch]');
        if (startSearch.length > 0) {
            searchModel = [];
            startSearch.each(function (index, elm) {
                addTosearchModel(elm);
            });
        }
        Ladda.bind('#search', {
            callback: function (ins) {
                searchModel = [];
                instance = ins;
                var serachInput = $('#search-content [searchColum]');
                serachInput.each(function (index, elm) {
                    addTosearchModel(elm);
                });
                self.DataTable().draw();
            }
        });

        var isselect = !!option.select;
        if (isselect) {
            var selected = [];
            self.find('tbody')
                .on('click', 'tr',
                    function (e) {
                        if (e.target.nodeName === 'TD') {
                            var id = this.id;
                            var index = $.inArray(id, selected);

                            if (index === -1) {
                                selected.push(id);
                            } else {
                                selected.splice(index, 1);
                            }

                            $(this).toggleClass('selected');
                        }
                    });
        }
        self.on('xhr.dt', function () {
            if (instance) {
                instance.stop();
                instance = null;
            }
        });
        return self.on('preXhr.dt', function (e, settings, data) {

            if (searchModel && searchModel.length > 0)
            	
            	
                 $.preArrData(data,searchModel);
            
            
            
            
            if (settings.bSorted && data.order.length) {
                data.orderType = data.order[0].dir;
                data.order = data.columns[data.order[0].column].data;
            }
            if (data.length > 0) {
                data.pageIndex = data.start === 0 ? 1 : (data.start / data.length + 1);
                data.pageSize = data.length;
            }
            data.length = {};
            data.start = {};
            data.columns = {};
            data.search = {};
        });
    }
});

$.fn.extend({
    preDatepickerEvent: function (e) {
        var self = $(this);
        return self.on('apply.daterangepicker', function (e, dataPick) {
            $(this).val(dataPick.startDate.format(dataPick.locale.format) + ' - ' + dataPick.endDate.format(dataPick.locale.format));
            var startTime = dataPick.startDate.format('YYYY-MM-DD hh:mm:ss.S');
            var endTime = dataPick.endDate.format('YYYY-MM-DD hh:mm:ss.S');
            $('#startTime').val(startTime);
            $('#endTime').val(endTime);
        }).on('cancel.daterangepicker', function (e, dataPick) {
            $('#startTime').val('');
            $('#endTime').val('');
            $(this).val('');
        });
    }
});