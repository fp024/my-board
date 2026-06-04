console.log("Reply Module.....");

var replyService = (function () {
  function add(reply, callback, error) {
    console.log("reply.....");
    $.ajax({
      type: 'post',
      url: '/replies/new',
      data: JSON.stringify(reply),
      contentType: 'application/json',
      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      }
    });
  }

  function getList(param, callback, error) {
    var bno = param.bno;
    var page = param.page || 1;
    $.getJSON("/replies/pages/" + bno + "/" + page, function (data) {
      if (callback) {
        // callback(data); 댓글 목록만 가져오는 경우
        callback(data.pageSize, data.pageNavigationSize, data.replyCount, data.list); // 댓글 숫자와 목록을 가져오는 경우
      }
    }).fail(function (xhr, status, err) {
      if (error) {
        error();
      }
    });
  }

  function remove(rno, replyer, callback, error) {
    $.ajax({
      type: 'delete',
      url: '/replies/' + rno,
      data: JSON.stringify({rno: rno, replyer: replyer}),
      contentType: 'application/json',
      success: function (deleteResult, status, xhr) {
        if (callback) {
          callback(deleteResult);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      }
    });
  }

  function update(reply, callback, error) {
    console.log("RNO: " + reply.rno);
    console.log("REPLYER: " + reply.replyer);

    $.ajax({
      type: 'put',
      url: '/replies/' + reply.rno,
      data: JSON.stringify(reply),
      contentType: 'application/json',
      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      }
    });
  }

  function get(rno, callback, error) {
    $.getJSON("/replies/" + rno, function (result) {
      if (callback) {
        callback(result);
      }
    }).fail(function (xhr, status, err) {
      if (error) {
        error();
      }
    });
  }

  // [년,월,일,시,분,초] 배열이 인자로 들어온다.
  function displayTime(timeValue) {
    var today = new Date()
    today.setHours(0, 0, 0, 0);

    var replyDate = new Date();
    replyDate.setFullYear(timeValue[0], timeValue[1] - 1, timeValue[2]);
    replyDate.setHours(timeValue[3], timeValue[4], timeValue[5], 0);

    if (today.getTime() > replyDate.getTime()) {
      var yy = replyDate.getFullYear();
      var mm = replyDate.getMonth() + 1;
      var dd = replyDate.getDate();
      return [yy, ((mm > 9 ? '' : '0') + mm), ((dd > 9 ? '' : '0') + dd)].join('/');
    } else {
      var hh = replyDate.getHours();
      var mi = replyDate.getMinutes();
      var ss = replyDate.getSeconds();
      return [((hh > 9 ? '' : '0') + hh), ((mi > 9 ? '' : '0') + mi), ((ss > 9 ? '' : '0') + ss)].join(':');
    }
  }


  return {
    add: add,
    getList: getList,
    remove: remove,
    update: update,
    get: get,
    displayTime: displayTime
  };
})();