/**
 *
 */
(function ($) {
	var _matchs = {};
	$.extend({
		matchInfo : function (matchKey, paramMatch) {
			if (matchKey) {
				_matchs[matchKey] = _matchs[matchKey] || {};
				if (paramMatch) {
					$.each(paramMatch, function (name, data) {
						_matchs[matchKey][name] = data;
					});
					return undefined;
				} else {
					return _matchs[matchKey];
				}
			}
			return _matchs;
		}
		,hideCnt:0
	});
})(jQuery);
