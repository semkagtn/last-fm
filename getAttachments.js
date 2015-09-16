var requests = 25;
var max_posts = 100;

var offset = parseInt(Args.offset);
var i = 0;
var result = [];
while (i < requests) {
    var response = API.wall.get({
        "owner_id": Args.owner_id,
        "offset": offset,
        "count": offset + max_posts,
        "filter": Args.filter
    });
    offset = offset + max_posts;
    i = i + 1;
    result =  result + response.items@.attachments;
}
return result;
