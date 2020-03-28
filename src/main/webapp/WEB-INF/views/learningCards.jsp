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
            var words = ${words};

            var Activity = (function() {
                
                return {
                    i: 0,
                    
                    isLastWord: function() {
                        return this.i === words.length - 1;
                    },
                    
                    next: function() {
                        this.currentWord = words[this.i++];
                        var word = this.currentWord;
                        
                        if (this.isLastWord()) {
                            $('#nextWordPanel').addClass("d-none");
                            $('#nextWordPanelDetailed').addClass("d-none");
                            $('#nextWordBtn').addClass("d-none");
                            $('#nextWordBtnDetailed').addClass("d-none");
                            
                            $('#returnToActivitiesLink').removeClass("d-none");
                            $('#returnToActivitiesLinkDetailed').removeClass("d-none");
                        }
    
                        $('#wordOriginal').html("<b>" + word.original + "</b>");
                        
                        setTimeout(function () {
                            $('#wordOriginalDetailed').html("<b>" + word.original + "</b>");
                            $('#wordTranslationDetailed').html("<b>" + word.translation + "</b>");

                            if (word.associationImgUrl) {
                                $('#associationImg').prop('src', word.associationImgUrl);
                                $('#associationImgContainer').removeClass('d-none');
                            } else {
                                $('#associationImg').prop('src', '');
                                $('#associationImgContainer').addClass('d-none');
                            }
                        }, 1000);
                        
                        $('#usageExamples').empty();
                        if (word.usageExamples.length === 0) {
                            $('#usageExamplesPanel').addClass("d-none");
                        } else {
                            $('#usageExamplesPanel').removeClass("d-none");
                            $.each(word.usageExamples, function (idx, example) {
                                $('#usageExamples').append($("<li>").text(example));
                            });
                        }

                        $('.flip-card-inner').removeClass('flip-card-rotated');
                    },
                    
                    learn: function () {
                        $('.flip-card-inner').addClass('flip-card-rotated');
                    }
                };
            })();

            $(window).on('load', function() {
                Activity.next();
            });
        </script>

        <div id="wordPanel" class="flip-card d-flex w-100" style="flex-direction: column; max-width: 700px; background-color: #90bbe0; margin: 25px auto;">
            <div class="flip-card-inner">
                <div class="flip-card-front">
                    <div class="d-flex" style="margin-top: 25px; justify-content: space-around;">
                        <div id="wordOriginal" class="text-center" style="background-color: #58bcf7;border-radius: 20px;width: 300px; padding: 10px;"></div>
                    </div>
                    <div id="nextWordPanel" class="d-flex" style="margin-top: 25px;margin-bottom: 25px; justify-content: center;">
                        <button id="learnWordBtn" class="btn btn-primary" onclick="Activity.learn()">
                            Learn
                        </button>
                        <button id="nextWordBtn" class="btn btn-primary" onclick="Activity.next()">
                            <spring:message code="next_word" />
                        </button>
                        <a id="returnToActivitiesLink" class="btn btn-primary d-none" style="margin-right: 25px;" href="/activities" role="button">
                            <spring:message code="return_to_activities" />
                        </a>
                    </div>
                </div>

                <div class="flip-card-back">
                    <div id="wordInfoPanel" class="card" style="max-width: 700px; background-color: #90bbe0;">
                        <h4 class="card-header primary-color white-text">
                            <spring:message code="word_information" />
                        </h4>
                        <div class="card-body">
                            <table class="table" style="margin-top: 15px; text-align: center;">
                                <tr>
                                    <td><h5 id="wordOriginalDetailed" style="font-size: 25px;"></h5></td>
                                    <td><h5 id="wordTranslationDetailed" style="font-size: 25px;"></h5></td>
                                </tr>
                            </table>

                            <div id="associationImgContainer" class="d-none">
                                <label><spring:message code="association_img" />:</label>
                                <img id="associationImg" style="width: 100%;" />
                            </div>

                            <div id="usageExamplesPanel" class="card" style="margin-top: 15px;">
                                <h5 class="card-header primary-color white-text">
                                    <spring:message code="usage_examples" />
                                </h5>
                                <div class="card-body">
                                    <ol id="usageExamples" class="usageExamples" style="list-style-type: decimal; margin-left: 15px; word-wrap: break-word;">

                                    </ol>
                                </div>
                            </div>
                        </div>
                        <div id="nextWordPanelDetailed" class="d-flex" style="margin-top: 25px;margin-bottom: 25px; justify-content: center;">
                            <button id="nextWordBtnDetailed" class="btn btn-primary" onclick="Activity.next()">
                                <spring:message code="next_word" />
                            </button>
                            <a id="returnToActivitiesLinkDetailed" class="btn btn-primary d-none" style="margin-right: 25px;" href="/activities" role="button">
                                <spring:message code="return_to_activities" />
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:page>