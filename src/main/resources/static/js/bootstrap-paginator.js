(function ($) {

    $.fn.paginator = function (options) {
        var options = $.extend({}, $.fn.paginator.defaults, options);
        return this.each(function () {

            $(this).empty().append(renderpager(parseInt(options.current_page), parseInt(options.page_count), parseInt(options.button_number), options.pager_click).children());
            $(this).addClass('pagination');
        });
    };

    function renderpager(current_page, page_count, button_number, pager_click) {

        var $pager = $('<ul></ul>');

        $pager.append(renderButton('first', current_page, page_count, pager_click)).append(renderButton('prev', current_page, page_count, pager_click));

        var page_min = Math.max(current_page - (button_number - 1) / 2 | 0, 1);
        var page_max = Math.min(page_min + button_number - 1, page_count);

        if (page_max + 1 - page_min < button_number)
            page_min = Math.max(page_max - button_number + 1, 1);

        for (var page = page_min; page <= page_max; page++) {
            $pager.append(renderButton(page, current_page, page_count, pager_click));
        }

        $pager.append(renderButton('next', current_page, page_count, pager_click)).append(renderButton('last', current_page, page_count, pager_click));

        return $pager;
    }

    function renderButton(link_page, current_page, page_count, pager_click) {
        var link_title, link_content, class_li = "";

        switch (link_page) {
            case 'first':
                link_title = "first";
                link_content = 'first';
                link_page = 1;
                if (current_page <= 1)    class_li = "disabled";
                break;
            case 'last':
                link_title = "last";
                link_content = 'last';
                link_page = page_count;
                if (current_page >= page_count)    class_li = "disabled";
                break;
            case 'prev':
                link_title = "previous page";
                link_content = '&laquo;';
                link_page = current_page - 1;
                if (current_page <= 1)    class_li = "disabled";
                break;
            case 'next':
                link_title = "next page";
                link_content = '&raquo;';
                link_page = current_page + 1;
                if (current_page >= page_count)    class_li = "disabled";
                break;
            default:
                link_title = link_page + "/" + page_count;
                link_content = link_page;
                if (current_page == link_page)    class_li = "active";
                break;
        }

        var title = '';
        var callback = function () {
        };
        if (class_li == '') {
            title = 'data-original-title="' + link_title + '"';
            callback = function () {
                pager_click(link_page);
            };
        }

        return btn = $('<li class="' + class_li + '"><a data-toggle="tooltip" ' + title + ' href="#">' + link_content + '</a></li>').click(callback);
    }

    $.fn.paginator.defaults = {
        current_page: 1,
        page_count: 1,
        button_number: 5
    };

})(jQuery);
