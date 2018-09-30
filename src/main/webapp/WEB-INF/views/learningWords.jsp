<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="activities">
	<jsp:attribute name="navBar">
        <t:navBar hideLangPanel="true"></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>
    <jsp:attribute name="jsIncludes">
        <script src="/js/lib/Chart.min.js"></script>
        <script src="/js/lib/moment.min.js"></script>
        <script src="/js/lib/ez.countimer.min.js" type="text/javascript"></script>
    </jsp:attribute>

    <jsp:body>
       
        <script>
            var questions = ${questionsJson};

            var Activity = (function() {
                
                return {
                    wordIdChangePriorMap: {},
                    
                    NUMBER_OF_OPTIONS: 4,

                    i: 0,
                    currentWord: undefined,
                    
                    successCnt: 0,
                    wrongCnt: 0,
                    
                    isLastWord: function() {
                        return this.i === questions.length - 1;
                    },
                    
                    next: function() {
                        $("#wordPanel button[id^='option']").removeClass("btn-success").removeClass("btn-danger").addClass("btn-info").prop('disabled', false);
                        // hide
                        $('#wordInfoPanel').addClass("d-none");

                        this.currentWord = questions[this.i++];
                        
                        if (this.isLastWord()) {
                            $('#nextWordPanel').addClass("d-none");
                            $('#nextWordBtn').addClass("d-none");
                        }
                        
                        $('#wordOriginal').html("<spring:message code='category' /> '" + this.currentWord.category + "'" + "<hr><b>" + this.currentWord.original + "</b>");

                        for (var i = 0; i < this.NUMBER_OF_OPTIONS; i++) {
                            $('#option' + (i+1)).text(this.currentWord.answers[i]);
                        }

                        $('#nextWordBtn').prop('disabled', true);

                        if (this.i > 1) {
                            $('#wordTimer').countimer('start');
                        }
                    },

                    answer: function(obj) {
                        var button = $(obj);
                        button.blur();
                        var chosenAnswer = button.text();
                        if (chosenAnswer === this.currentWord.translation) {
                            new Audio("/audio/correct-answer.mp3").play();
                            
                            this.wordIdChangePriorMap[this.currentWord.id] = -1;
                            this.successCnt++;
                            
                            if (!this.isLastWord()) {
                                this.next();
                                return;
                            } else {
                                this.updatePriority();
                                button.removeClass("btn-info");
                                button.addClass("btn-success");
                                $('#nextWordPanel').removeClass("d-none");
                                $('#returnToActivitiesLink').removeClass("d-none");
                                $('#showResultsBtn').removeClass("d-none");

                                $('#totalTimer,#wordTimer').countimer('stop');
                            }
                        } else {
                            var self = this;

                            new Audio("/audio/wrong-answer.mp3").play();
                            
                            this.wordIdChangePriorMap[this.currentWord.id] = 1;
                            this.wrongCnt++;
                            if (this.isLastWord()) {
                                this.updatePriority();
                                $('#nextWordPanel').removeClass("d-none");
                                $('#returnToActivitiesLink').removeClass("d-none");
                                $('#showResultsBtn').removeClass("d-none");

                                $('#totalTimer,#wordTimer').countimer('stop');
                            }
                            
                            button.removeClass("btn-info");
                            button.addClass("btn-danger");

                            var found = false;
                            $('#wordPanel button').each(function(index) {
                                if (found) return;
                                if ($(this).text() === self.currentWord.translation) {
                                    $(this).removeClass("btn-info");
                                    $(this).addClass("btn-success");
                                    found = true;
                                }
                            });

                            this.showWordInfo();
                        }
                        $("#wordPanel button[id^='option']").prop('disabled', true);
                        $('#nextWordBtn').prop('disabled', false);
                    },
                    
                    updatePriority: function () {
                        var self = this;
                        var correctIds = [];
                        var wrongIds = [];
                        Object.keys(this.wordIdChangePriorMap).forEach(function (key) {
                            if (self.wordIdChangePriorMap[key] > 0) {
                                wrongIds.push(key);
                            } else {
                                correctIds.push(key);
                            }
                        });

                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/word/updatePriority",
                            data : JSON.stringify({ correctIds: correctIds, wrongIds: wrongIds }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                console.log("SUCCESS: ", data);
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            }
                        });
                    },
                    
                    showResults: function() {
                        $("#wordPanel").addClass("d-none");
                        $("#wordPanel").removeClass("d-flex");
                        // hide
                        $('#wordInfoPanel').addClass("d-none");
                        $("#resultsPanel").removeClass("d-none");

                        $('#wordTimerPanel,#totalTimerPanel').addClass("d-none");

                        var ctx = document.getElementById('resultsChart').getContext('2d');
                        var resultsChart = new Chart(ctx, {
                            type: 'pie',
                            data: {
                                datasets: [
                                    {
                                        data: [this.successCnt, this.wrongCnt],
                                        backgroundColor: ["#008000", "#FF0000"]
                                    }
                                ],
                                labels: ["Correct", "Wrong"]
                            }
                        });
                        
                        if (this.wrongCnt > 0) {
                            $("#any-wrong-result-msg").removeClass("d-none");
                            $("#ideal-result-msg").addClass("d-none");
                        } else {
                            $("#any-wrong-result-msg").addClass("d-none");
                            $("#ideal-result-msg").removeClass("d-none");
                        }
                    },
                    
                    showWordInfo: function() {
                        $('#detailsLink').attr('href', "http://www.thesaurus.com/browse/" + encodeURIComponent(this.currentWord.original));
                        // show
                        $('#wordInfoPanel').removeClass("d-none");

                        $('#wordInfo_word').html(this.currentWord.original);
                        
                        this.showSentenceExamples();
                        this.showAssociationImage();

                        $([document.documentElement, document.body]).animate({
                            scrollTop: $("#wordInfoPanel").offset().top
                        }, 2000);
                    },

                    addSentenceExample: function () {
                        var self = this;
                        var sentenceExample = $('#sentenceExample').val();
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/word/addSentenceExample",
                            data : JSON.stringify({ wordId: this.currentWord.id, sentence: sentenceExample }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                $('#sentenceExample').val("");
                                self.showSentenceExamples();
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            }
                        });
                    },
                    
                    showSentenceExamples: function() {
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/word/usageExamples",
                            data : JSON.stringify({ wordId: this.currentWord.id }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                if (data.sentences.length === 0) {
                                    $('#usageExamplesPanel').addClass("d-none");
                                } else {
                                    $('#usageExamplesPanel').removeClass("d-none");
                                }
                                $('#usageExamples').empty();
                                $.each(data.sentences, function (idx, sentence) {
                                    $('#usageExamples').append($("<li>").text(sentence));
                                });
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                            }
                        });
                    },
                    
                    showAssociationImage: function() {
                        if (this.currentWord.associationImgUrl) {
                            $('#associationImg').prop('src', this.currentWord.associationImgUrl);
                            $('#associationImgContainer').removeClass('d-none');
                        } else {
                            $('#associationImg').prop('src', '');
                            $('#associationImgContainer').addClass('d-none');
                        }
                    }
                };
            })();
            
            $(window).on('load', function() {
                Activity.next();

                $('#totalTimer,#wordTimer').countimer({
                    displayMode: 3,
                    useHours: false
                });
            });

        </script>
        
        <div id="totalTimerPanel" class="text-center d-none d-xl-block" style="position: fixed; top: 60px; right: 1%; width: 150px; border-radius: 4px 4px;border: 2px solid #efe5e5; margin: 15px auto;">
            <spring:message code="total_time" />: <span id="totalTimer" style="font-size: 35px;width: 0.6em;line-height: 40px;font-family: digital, arial, verdana;text-align: center;color: #fff;text-shadow: 0 0 5px rgba(255, 255, 255, 1);"></span>
        </div>

        <div id="wordTimerPanel" class="text-center d-none d-xl-block" style="position: fixed; top: 150px; right: 1%; width: 150px; border-radius: 4px 4px;border: 2px solid #efe5e5; margin: 15px auto;">
            <spring:message code="word_time" />: <span id="wordTimer" style="font-size: 35px;width: 0.6em;line-height: 40px;font-family: digital, arial, verdana;text-align: center;color: #fff;text-shadow: 0 0 5px rgba(255, 255, 255, 1);"></span>
        </div>
        
        <div id="wordPanel" class="card d-flex w-100" style="flex-direction: column; max-width: 700px; background-color: #90bbe0; border: 2px solid #22ade2; border-radius: 35px; margin: 25px auto;">
            <div class="d-flex" style="margin-top: 25px; justify-content: space-around;">
                <div id="wordOriginal" class="text-center" style="background-color: #58bcf7;border-radius: 20px;width: 300px; padding: 10px;"></div>
            </div>
            <div style="margin-top: 20px; margin-left: 5%; display:grid; grid-gap: 15px; grid-template-columns: 45% 45%;">
                <button id="option1" class="btn btn-info" onclick="Activity.answer(this);"></button>
                <button id="option2" class="btn btn-info" onclick="Activity.answer(this);"></button>
                <button id="option3" class="btn btn-info" onclick="Activity.answer(this);"></button>
                <button id="option4" class="btn btn-info" onclick="Activity.answer(this);"></button>
            </div>
            <div id="nextWordPanel" class="d-flex" style="margin-top: 25px;margin-bottom: 25px; justify-content: center;">
                <button id="nextWordBtn" class="btn btn-primary" onclick="Activity.next()">
                    <spring:message code="next_word" />
                </button>
                <a id="returnToActivitiesLink" class="btn btn-primary d-none" style="margin-right: 25px;" href="/activities" role="button">
                    <spring:message code="return_to_activities" />
                </a>
                <button id="showResultsBtn" class="btn btn-primary d-none" onclick="Activity.showResults()">
                    <spring:message code="show_results" />
                </button>
            </div>
        </div>

        <div id="wordInfoPanel" class="card d-none" style="margin: 25px auto; max-width: 700px; background-color: #90bbe0;">
            <h4 class="card-header primary-color white-text">
                <spring:message code="word_information" />
            </h4>
            <div class="card-body">
                <div style="margin-top: 15px; text-align: center;">
                    <h5 id="wordInfo_word" style="font-size: 25px;"></h5>
                </div>

                <hr>
                
                <div style="margin-top: 15px;">
                    <a id="detailsLink" target="_blank">
                        <spring:message code="click_more_info" />
                    </a>
                </div>
    
                <div id="associationImgContainer" class="d-none">
                    <label><spring:message code="association_img" />:</label>
                    <img id="associationImg" style="width: 100%;" />
                </div>
                
                <div style="margin-top: 15px;">
                    <spring:message code="learn_by_doing_approach" />
                </div>
    
                <div id="usageExamplesPanel" class="card" style="margin-top: 15px;">
                    <h5 class="card-header primary-color white-text">
                        <spring:message code="usage_examples" />
                    </h5>
                    <div class="card-body">
                        <ol id="usageExamples" style="list-style-type: decimal; margin-left: 15px; word-wrap: break-word;">
                            
                        </ol>
                    </div>
                </div>
                
                <div style="margin-top: 25px;">
                    <spring:message code="make_sentence_approach" />
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="sentenceExample">
                            <spring:message code="sentence" />:
                        </label>
                        <textarea class="form-control" rows="5" id="sentenceExample"></textarea>
                    </div>
                    <div class="d-flex" style="justify-content: flex-end;">
                        <button id="addSentenceExampleBtn" class="btn btn-primary" onclick="Activity.addSentenceExample();">
                            <spring:message code="save" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="resultsPanel" class="d-none">
            <div class="d-flex" style="flex-wrap: wrap;">
                <div style="width: 500px; height: 50%;">
                    <canvas id="resultsChart"></canvas>
                </div>
                <div class="card h-100" style="margin-top: 15px; background-color: #90bbe0;">
                    <div class="card-body">
                        <span id="any-wrong-result-msg" class="d-none"><spring:message code="next_time_focus_msg" /></span>
                        <span id="ideal-result-msg" class="d-none"><spring:message code="ideal_result_msg" /></span>
                        <div>
                            <a class="btn btn-primary" href="/activities" role="button">
                                <spring:message code="return_to_activities" />
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:page>