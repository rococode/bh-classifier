(window.webpackJsonp = window.webpackJsonp || []).push([
    [0], {
        15: function(e, a, t) {},
        17: function(e, a, t) {},
        19: function(e, a, t) {
            "use strict";
            t.r(a);
            var n = t(0),
                l = t.n(n),
                i = t(8),
                r = t.n(i),
                s = (t(15), t(1)),
                o = t(2),
                c = t(5),
                d = t(3),
                m = t(4),
                u = t(6),
                h = (t(17), []),
                p = function(e) {
                    function a(e) {
                        var t;
                        return Object(s.a)(this, a), (t = Object(c.a)(this, Object(d.a)(a).call(this, e))).alreadyShown = [], t
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "clean",
                        value: function(e) {
                            return e = (e = (e = (e = e.trim()).replace(/[^a-zA-Z]/g, "")).replace(/\./, "")).toLowerCase()
                        }
                    }, {
                        key: "check",
                        value: function(e, a) {
                            a = this.clean(a);
                            for (var t = 0; t < h.length; t++)
                                if (h[t] == e) return 2;
                            for (var n = 0; n < this.props.explain.length; n++)
                                if (a == this.props.explain[n]) return this.alreadyShown.indexOf(a) > -1 ? 0 : (this.alreadyShown.push(a), 1);
                            return 0
                        }
                    }, {
                        key: "addChosen",
                        value: function(e, a) {
                            if (this.props.feedback) {
                                console.log("cleaning " + e + " with " + a), a = this.clean(a), console.log("adding " + e + " with " + a);
                                var t = O.indexOf(a),
                                    n = h.indexOf(e);
                                if (-1 == n)
                                    for (var l = 0; l < O.length; l++)
                                        if (a == O[l]) return C("repeated " + a), void alert("Please select 3 different words.\nIf you change your mind on a word, you can click it again to deselect it.");
                                if (t > -1) O.splice(t, 1), h.splice(n, 1), C("toggled_off " + a);
                                else {
                                    if (3 == O.length) return C("already_three " + a), void alert("You have already chosen the required 3 words.\nIf you change your mind on a word, you can click it again to deselect it.");
                                    C("toggled_on " + a), O.push(a), h.push(e)
                                }
                                this.props.redraw()
                            }
                        }
                    }, {
                        key: "render",
                        value: function() {
                            var e = [],
                                a = [],
                                t = this,
                                n = "email-word" + (this.props.feedback ? "" : "-nof"),
                                i = 0;
                            return this.alreadyShown = [], this.props.subject.split(" ").forEach(function(a) {
                                var r = i++,
                                    s = t.check(r, a);
                                1 == s ? e.push(l.a.createElement("span", {
                                    key: r + "-word",
                                    className: n + " email-subj-explain",
                                    onClick: function() {
                                        t.addChosen(r, a)
                                    }
                                }, a)) : 2 == s ? e.push(l.a.createElement("span", {
                                    key: r + "-word",
                                    className: n + " email-subj-chosen",
                                    onClick: function() {
                                        t.addChosen(r, a)
                                    }
                                }, a)) : e.push(l.a.createElement("span", {
                                    key: r + "-word",
                                    className: n,
                                    onClick: function() {
                                        t.addChosen(r, a)
                                    }
                                }, a)), e.push(l.a.createElement("span", {
                                    key: r + "-wordsp"
                                }, "\xa0"))
                            }), this.props.message.split(" ").forEach(function(e) {
                                for (var r = e.split("\n"), s = function(e) {
                                    var s = i++,
                                        o = r[e],
                                        c = t.check(s, o);
                                    1 == c ? a.push(l.a.createElement("span", {
                                        key: s + "-word",
                                        className: n + " email-subj-explain",
                                        onClick: function() {
                                            t.addChosen(s, o)
                                        }
                                    }, o)) : 2 == c ? a.push(l.a.createElement("span", {
                                        key: s + "-word",
                                        className: n + " email-subj-chosen",
                                        onClick: function() {
                                            t.addChosen(s, o)
                                        }
                                    }, o)) : a.push(l.a.createElement("span", {
                                        key: s + "-word",
                                        className: n,
                                        onClick: function() {
                                            t.addChosen(s, o)
                                        }
                                    }, o)), e < r.length - 1 && a.push(l.a.createElement("span", {
                                        key: s + "-wordnl"
                                    }, "\n"))
                                }, o = 0; o < r.length; o++) s(o);
                                a.push(l.a.createElement("span", {
                                    key: e + i + "wordsp"
                                }, "\xa0"))
                            }), l.a.createElement("div", {
                                className: "email-wrap"
                            }, l.a.createElement("div", {
                                className: "email"
                            }, this.props.pred.length > 0 && l.a.createElement("div", {
                                className: "email-header"
                            }, l.a.createElement("div", {
                                className: "email-subject"
                            }, "Subject: ", e), l.a.createElement("div", {className: "email-pred"}, this.props.pred === "baseball" ? l.a.createElement("div", {
                                className: "badge-b"
                            }, "Model Decision: " + this.props.pred) : l.a.createElement("div", {
                                className: "badge-h"
                            }, "Model Decision: " + this.props.pred))), l.a.createElement("div", {
                                className: "email-message"
                            }, a)))
                        }
                    }]), a
                }(l.a.Component),
                E = function(e) {
                    function a(e) {
                        return Object(s.a)(this, a), Object(c.a)(this, Object(d.a)(a).call(this, e))
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "render",
                        value: function() {
                            var e = 0 == F[0] ? "selected" : "",
                                a = 1 == F[0] ? "selected" : "";
                            return P.indexOf("instance") > -1 ? l.a.createElement("div", {
                                className: "feedback-instance"
                            }, l.a.createElement("div", {
                                className: "feedback-instance-label"
                            }, "Please provide feedback ", l.a.createElement("strong", null, "to the model"), " by telling it whether the decision (baseball or hockey) is correct."), l.a.createElement("div", {
                                className: "feedback-instance-button feedback-instance-correct " + a,
                                onClick: function() {
                                    F[0] = 1, A.forceUpdate()
                                }
                            }, "Correct"), l.a.createElement("div", {
                                className: "feedback-instance-button feedback-instance-incorrect " + e,
                                onClick: function() {
                                    F[0] = 0, A.forceUpdate()
                                }
                            }, "Incorrect")) : l.a.createElement("div", {
                                className: "feedback-instance"
                            }, l.a.createElement("div", {
                                className: "feedback-instance-label"
                            }, "Please tell the model whether it should associate these three words with hockey or baseball."), l.a.createElement("div", {
                                className: "survey-button-h horizontal-margin survey-hockey " + a,
                                onClick: function() {
                                    F[0] = 1, A.forceUpdate()
                                }
                            }, "Hockey"), l.a.createElement("div", {
                                className: "survey-button-b horizontal-margin survey-baseball " + e,
                                onClick: function() {
                                    F[0] = 0, A.forceUpdate()
                                }
                            }, "Baseball"))
                        }
                    }]), a
                }(l.a.Component),
                v = function(e) {
                    function a(e) {
                        return Object(s.a)(this, a), Object(c.a)(this, Object(d.a)(a).call(this, e))
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "render",
                        value: function() {
                            var e = 0 == q[0] ? "selected" : "",
                                a = 1 == q[0] ? "selected" : "",
                                t = 0 == F[0] ? "selected" : "",
                                n = 1 == F[0] ? "selected" : "";
                            return console.log("instcor" + F[0]), l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                className: "feedback-instance"
                            }, l.a.createElement("div", {
                                className: "feedback-instance-label"
                            }, "First, do you think this email is about hockey or baseball?"), l.a.createElement("div", {
                                className: "survey-button-h horizontal-margin feedback-instance-hockey " + e,
                                onClick: function() {
                                    q[0] = 0, A.forceUpdate()
                                }
                            }, "Hockey"), l.a.createElement("div", {
                                className: "survey-button-b horizontal-margin feedback-instance-baseball " + a,
                                onClick: function() {
                                    q[0] = 1, A.forceUpdate()
                                }
                            }, "Baseball")), l.a.createElement("br", null), l.a.createElement("div", {
                                className: "feedback-instance"
                            }, l.a.createElement("div", {
                                className: "feedback-instance-label"
                            }, "Second, what do you think the model will decide this email is about?"), l.a.createElement("div", {
                                className: "survey-button-h horizontal-margin feedback-instance-correct " + t,
                                onClick: function() {
                                    F[0] = 0, A.forceUpdate()
                                }
                            }, "Hockey"), l.a.createElement("div", {
                                className: "survey-button-b horizontal-margin feedback-instance-incorrect " + n,
                                onClick: function() {
                                    F[0] = 1, A.forceUpdate()
                                }
                            }, "Baseball")))
                        }
                    }]), a
                }(l.a.Component),
                f = function(e) {
                    function a(e) {
                        return Object(s.a)(this, a), Object(c.a)(this, Object(d.a)(a).call(this, e))
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "render",
                        value: function() {
                            console.log("testtype: " + tt);
                            console.log("testaccuracy: " + ta);
                            return l.a.createElement("div", {
                                className: "pred"
                            }, l.a.createElement("span", {
                                className: "pred-q"
                            }, (0 == P.indexOf("practice") || 0 == P.indexOf("train")) ? "The model decided that this email is about " : (tt === "same" ? "In Phase 2, the model " + (ta === "T" ? "correctly" : "incorrectly") + " decided that this email was about " : "This email contains many of the same words as an email that you reviewed in Phase 2, which the model " + (ta === "T" ? "correctly" : "incorrectly") + " decided was about ")), this.props.pred === "baseball" ? l.a.createElement("span", {
                                className: "badge-b"
                            }, this.props.pred) : l.a.createElement("span", {
                                className: "badge-h"
                            }, this.props.pred), l.a.createElement("span", {
                                className: "pred-q"
                            }, (0 == P.indexOf("practice") || 0 == P.indexOf("train")) ? " , which may or may not be correct." : "."))
                        }
                    }]), a
                }(l.a.Component),
                b = function(e) {
                    function a(e) {
                        return Object(s.a)(this, a), Object(c.a)(this, Object(d.a)(a).call(this, e))
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "render",
                        value: function() {
                            var e = O.join(", "),
                                a = 3 != O.length ? " Please choose " + (3 - O.length) + " more word" + (3 - O.length != 1 ? "s" : "") + " to proceed." : "",
                                t = "You have chosen " + O.length + " out of 3 words" + (O.length > 0 ? ": " + e + "." : ".") + a;
                            return l.a.createElement("div", {
                                className: "chosen"
                            }, l.a.createElement("div", {
                                className: "chosen-curr"
                            }, t))
                        }
                    }]), a
                }(l.a.Component),
                g = function(e) {
                    var fdbk = "";

                    function a(e) {
                        var t;
                        return Object(s.a)(this, a), (t = Object(c.a)(this, Object(d.a)(a).call(this, e))).state = {
                            currChoice: ""
                        }, t
                    }
                    return Object(m.a)(a, e), Object(o.a)(a, [{
                        key: "render",
                        value: function() {
                            var e = this,
                                a = P.indexOf("feature") > -1 ? (-1 != F[0] && 3 == O.length) : _ || -1 != F[0] || 3 == O.length;
                            return this.props.test && (a = -1 != F[0]), l.a.createElement(l.a.Fragment, null, t = (l.a.createElement("span", {
                                className: "survey"
                            }, l.a.createElement(l.a.Fragment, null, l.a.createElement("strong", null, "To help our research team interpret your evaluation later"), ", please let us know: Do ", l.a.createElement("em", null, "you"), " think that this email is about hockey or baseball?"), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "survey-button-h horizontal-margin survey-hockey" + ("correct" == this.state.currChoice ? "selected" : ""),
                                id: "hockeyButton",
                                onClick: function() {
                                    fdbk = "hockey";
                                    document.getElementById("proceedButton").className = "us-button next-email " + ((a && (fdbk !== "")) ? "ready" : "")
                                    document.getElementById("hockeyButton").className = "survey-button-h horizontal-margin survey-hockey " + ("correct" === e.state.currChoice ? "" : "selected")
                                    document.getElementById("baseballButton").className = "survey-button-b horizontal-margin survey-baseball " + ("incorrect" !== e.state.currChoice ? "" : "selected")
                                    document.getElementById("unsureButton").className = "survey-button-u horizontal-margin survey-unsure " + ("unsure" !== e.state.currChoice ? "" : "selected")
                                }
                            }, "Hockey"), l.a.createElement("div", {
                                className: "survey-button-b horizontal-margin survey-baseball" + ("incorrect" == this.state.currChoice ? "selected" : ""),
                                id: "baseballButton",
                                onClick: function() {
                                    fdbk = "baseball";
                                    document.getElementById("proceedButton").className = "us-button next-email " + ((a && (fdbk !== "")) ? "ready" : "")
                                    document.getElementById("hockeyButton").className = "survey-button-h horizontal-margin survey-hockey " + ("correct" !== e.state.currChoice ? "" : "selected")
                                    document.getElementById("baseballButton").className = "survey-button-b horizontal-margin survey-baseball " + ("incorrect" === e.state.currChoice ? "" : "selected")
                                    document.getElementById("unsureButton").className = "survey-button-u horizontal-margin survey-unsure " + ("unsure" !== e.state.currChoice ? "" : "selected")
                                }
                            }, "Baseball"), l.a.createElement("div", {
                                className: "survey-button-u horizontal-margin survey-unsure" + ("unsure" == this.state.currChoice ? "selected" : ""),
                                id: "unsureButton",
                                onClick: function() {
                                    fdbk = "unsure";
                                    document.getElementById("proceedButton").className = "us-button next-email " + ((a && (fdbk !== "")) ? "ready" : "");
                                    document.getElementById("hockeyButton").className = "survey-button-h horizontal-margin survey-hockey " + ("correct" !== e.state.currChoice ? "" : "selected")
                                    document.getElementById("baseballButton").className = "survey-button-b horizontal-margin survey-baseball " + ("incorrect" !== e.state.currChoice ? "" : "selected")
                                    document.getElementById("unsureButton").className = "survey-button-u horizontal-margin survey-unsure " + ("unsure" === e.state.currChoice ? "" : "selected")
                                }
                            }, "Not Sure"))), u = l.a.createElement("div", {
                                className: "us-button next-email " + ((a && (fdbk !== "")) ? "ready" : ""),
                                id: "proceedButton",
                                onClick: function() {
                                    console.log(fdbk);
                                    a && (fdbk !== "") && M(fdbk)
                                }
                            }, this.props.practice ? "Complete Practice" : this.props.last ? "Continue" : "Proceed to Next Email"));
                            return l.a.createElement("div", null, t, u)
                        }
                    }]), a
                }(l.a.Component),
                w = "vzhivov@superior.carleton.ca (Vladimir Zhivov)",
                y = "Sabres!!! Sweep.",
                N = "It's over - the Sabres came back to beat the Bruins in OT 6-5 tonight to sweep the series. A beautiful goal by Brad May (Lafontaine set him up while lying down on the ice) ended it. Fuhr left the game game with an injured shoulder and Lafontaine was banged up as well; however, the Sabres will get a week's rest so injuries should not be a problem.\nMontreal edged Quebec 3-2 to square their series, which seems to be headed for Game 7. The Habs dominated the first two periods and were unlucky to only have a 2-2 tie after 40 minutes. However, an early goal by Brunet in the 3rd won it.\nThe Islanders won their 3rd OT game of the series on a goal by Ray Ferraro 4-3; the Caps simply collapsed after taking a 3-0 lead in the 2nd. The Isles' all-time playoff OT record is now 28-7.\n- Vlad the Impaler",
                k = ["habs", "forever", "fuhr", "may", "ot"],
                x = "Hockey",
                tt = "SAME",
                ta = "T",
                O = [],
                F = [-1],
                q = [-1];

            function C(e) {
                var a = new XMLHttpRequest,
                    t = "event=" + encodeURIComponent(e);
                a.open("POST", "log", !0), a.setRequestHeader("Content-type", "application/x-www-form-urlencoded"), a.send(t)
            }
            var j = -1,
                I = -1,
                T = document.content_inject;
            "@DATA_INJECTION@" == T && (T = void 0);
            var P = void 0,
                S = "abc";
            if (T) x = T.pred ? T.pred : "", w = T.sender, S = T.id, y = T.subject, N = T.email, tt = T.testtype ? T.testtype : "", ta = T.testaccuracy ? T.testaccuracy : "", k = T.explain_words, P = T.mode, j = T.curr_idx, I = T.total_idx;
            else {
                (P = function(e) {
                    e = e.split("+").join(" ");
                    for (var a, t = {}, n = /[?&]?([^=]+)=([^&]*)/g; a = n.exec(e);) t[decodeURIComponent(a[1])] = decodeURIComponent(a[2]);
                    return t
                }(document.location.search).mode) || (P = "failed")
            } - 1 == P.indexOf("feature") && -1 == P.indexOf("instance") && P.indexOf("explain");
            var A, _ = -1 == P.indexOf("feature") && -1 == P.indexOf("instance");
            function M(e) {
                var a = "./next?feedback=" + encodeURIComponent(e);
                P.indexOf("feature") > -1 ? a += "&chosen=" + encodeURIComponent(JSON.stringify(O)) + "&instance=" + encodeURIComponent(1 == F[0] ? "instance_yes" : "instance_no") : P.indexOf("instance") > -1 && (a += "&instance=" + encodeURIComponent(1 == F[0] ? "instance_yes" : "instance_no")), location.href = a
            }

            var Y = function(e) {
                function a(e) {
                    var t;
                    return Object(s.a)(this, a), (t = Object(c.a)(this, Object(d.a)(a).call(this, e))).redraw = t.redraw.bind(Object(u.a)(Object(u.a)(t))), A = Object(u.a)(Object(u.a)(t)), t.state = {
                        showInstructions: !1
                    }, t
                }
                return Object(m.a)(a, e), Object(o.a)(a, [{
                    key: "redraw",
                    value: function() {
                        this.forceUpdate()
                    }
                }, {
                    key: "render",
                    value: function() {
                        var e = l.a.createElement(l.a.Fragment, null, "You will see three highlighted words that help ", l.a.createElement("strong", null, "explain"), " how the model made each decision. The highlighted words were the ", l.a.createElement("strong", null, "most important"), " to the model when deciding if the email was about hockey or baseball."),
                            a = l.a.createElement(l.a.Fragment, null, "You will be asked to help the model improve by ", l.a.createElement("strong", null, "giving feedback"), " to the model on each decision that it makes. To do this, you will tell the model whether it is right or wrong."),
                            t = l.a.createElement(l.a.Fragment, null, "You will be asked to help the model improve by ", l.a.createElement("strong", null, "giving feedback"), " to the model on each decision that it makes. To do this, you will select the three most important words that you think the model should use to decide if the email is about hockey or baseball."),
                            n = this,
                            i = l.a.createElement("div", {
                                className: "full-instructions",
                                onClick: function() {
                                    n.setState({
                                        showInstructions: !1
                                    })
                                }
                            }, l.a.createElement("div", {
                                className: "full-instructions-center"
                            }, l.a.createElement("div", {
                                className: "full-instructions-center-inner",
                                onClick: function(e) {
                                    return e.stopPropagation()
                                }
                            }, l.a.createElement("div", {
                                className: "x-button",
                                onClick: function() {
                                    n.setState({
                                        showInstructions: !1
                                    })
                                }
                            }, "X"), l.a.createElement("div", {
                                className: "fi-header"
                            }, "Instructions"), l.a.createElement("div", {
                                className: "instructions"
                            }, "This study is designed to explore how people understand machine learning models. Machine learning models are computer programs that analyze data and learn to make decisions related to the data."), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "instructions"
                            }, "You do not need to know anything about machine learning models to do this task."), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "instructions"
                            }, "For this task, you will interact with a machine learning model that has been trained to decide if emails are about hockey or baseball. This model is designed to help a person quickly sort through many emails about hockey and baseball."), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "fi-header"
                            }, "Task Introduction"), l.a.createElement("div", {
                                className: "instructions"
                            }, "Imagine that your boss has assigned you the tedious job of sorting the thousands of emails in his/her inbox. It\u2019s too much for you to do yourself, so you are trying out a machine learning model that is designed to help you finish the job quickly. In this task, you will evaluate the model to see if it is worthwhile to add to your workflow."), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "instructions"
                            }, "This task has ", l.a.createElement("strong", null, "three phases"), ":"), l.a.createElement("ol", null, l.a.createElement("li", null, "You will see a practice email to familiarize yourself with the task layout."), l.a.createElement("li", null, "You will see a series of 20 emails with the model's decisions.", P.indexOf("explain") > -1 && " You will see three highlighted words that help explain how the model made each decision.", (P.indexOf("feature") > -1 || P.indexOf("instance") > -1) && " For each email, you will provide feedback to the model by ", P.indexOf("feature") > -1 && "selecting the three most important words that you think the model should use to decide if the email is about baseball or hockey.", P.indexOf("instance") > -1 && "telling the model whether its decision is right or wrong."), l.a.createElement("li", null, P.indexOf("feature") > -1 || P.indexOf("instance") > -1 && "The model will have incorporated the feedback you gave in Phase 2. ", "You will be asked to evaluate the model's performance and usefulness.")), l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "fi-header"
                            }, "Keep in mind..."), l.a.createElement("div", {
                                className: "instructions"
                            }, "Evaluating the quality of the model is very important in this study. For this reason, we will award at least half of the workers on this task a ", l.a.createElement("strong", null, "bonus of $2"), " based on how thoroughly they evaluate the model's performance and usefulness in the last phase. Try your best!"), l.a.createElement("div", {
                                className: "invis-divider"
                            })))));
                        if (0 == P.indexOf("demographics")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Demographics Survey"), l.a.createElement("div", {
                            className: "instructions"
                        }, "Please start by filling in this brief demographics survey."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("form", {
                            action: "./next"
                        }, l.a.createElement("div", {
                            className: "instructions"
                        }, "What is your age?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-1",
                            value: "age-18-24",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-1"
                        }, l.a.createElement("span", null, "18-24 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-2",
                            value: "age-25-34",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-2"
                        }, l.a.createElement("span", null, "25-34 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-3",
                            value: "age-35-44",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-3"
                        }, l.a.createElement("span", null, "35-44 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-4",
                            value: "age-45-54",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-4"
                        }, l.a.createElement("span", null, "45-54 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-5",
                            value: "age-55-64",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-5"
                        }, l.a.createElement("span", null, "55-64 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-6",
                            value: "age-65-74",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-6"
                        }, l.a.createElement("span", null, "65-74 years old"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "age",
                            id: "age-7",
                            value: "age-75+",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "age-7"
                        }, l.a.createElement("span", null, "75 or older"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "What is the highest degree or level of school you have completed?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-1",
                            value: "education-no-hs",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-1"
                        }, l.a.createElement("span", null, "Less than high school degree"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-2",
                            value: "education-hs",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-2"
                        }, l.a.createElement("span", null, "High school degree or equivalent (e.g., GED)"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-3",
                            value: "education-some-college",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-3"
                        }, l.a.createElement("span", null, "Some college but no degree"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-4",
                            value: "education-assoc",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-4"
                        }, l.a.createElement("span", null, "Associate degree"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-5",
                            value: "education-bachelor",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-5"
                        }, l.a.createElement("span", null, "Bachelor degree"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "education",
                            id: "education-6",
                            value: "education-grad",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "education-6"
                        }, l.a.createElement("span", null, "Graduate degree"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "What is your gender?"), l.a.createElement("div", {
                            className: "demographic-textarea"
                        }, l.a.createElement("textarea", {
                            rows: 1,
                            id: "gender",
                            name: "gender",
                            required: !0
                        })), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "How familiar are you with computers?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "pc",
                            id: "pc-1",
                            value: "pc-none",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "pc-1"
                        }, l.a.createElement("span", null, "No knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "pc",
                            id: "pc-2",
                            value: "pc-little",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "pc-2"
                        }, l.a.createElement("span", null, "A little knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "pc",
                            id: "pc-3",
                            value: "pc-some",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "pc-3"
                        }, l.a.createElement("span", null, "Some knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "pc",
                            id: "pc-4",
                            value: "pc-lots",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "pc-4"
                        }, l.a.createElement("span", null, "A lot of knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "pc",
                            id: "pc-5",
                            value: "pc-expert",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "pc-5"
                        }, l.a.createElement("span", null, "Expert knowledge"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "How familiar are you with machine learning models?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "ml",
                            id: "ml-1",
                            value: "ml-none",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "ml-1"
                        }, l.a.createElement("span", null, "No knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "ml",
                            id: "ml-2",
                            value: "ml-little",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "ml-2"
                        }, l.a.createElement("span", null, "A little knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "ml",
                            id: "ml-3",
                            value: "ml-some",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "ml-3"
                        }, l.a.createElement("span", null, "Some knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "ml",
                            id: "ml-4",
                            value: "ml-lots",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "ml-4"
                        }, l.a.createElement("span", null, "A lot of knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "ml",
                            id: "ml-5",
                            value: "ml-expert",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "ml-5"
                        }, l.a.createElement("span", null, "Expert knowledge"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "How familiar are you with hockey?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "hockey",
                            id: "hockey-1",
                            value: "hockey-none",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "hockey-1"
                        }, l.a.createElement("span", null, "No knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "hockey",
                            id: "hockey-2",
                            value: "hockey-little",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "hockey-2"
                        }, l.a.createElement("span", null, "A little knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "hockey",
                            id: "hockey-3",
                            value: "hockey-some",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "hockey-3"
                        }, l.a.createElement("span", null, "Some knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "hockey",
                            id: "hockey-4",
                            value: "hockey-lots",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "hockey-4"
                        }, l.a.createElement("span", null, "A lot of knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "hockey",
                            id: "hockey-5",
                            value: "hockey-expert",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "hockey-5"
                        }, l.a.createElement("span", null, "Expert knowledge"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "How familiar are you with baseball?"), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "baseball",
                            id: "baseball-1",
                            value: "baseball-none",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "baseball-1"
                        }, l.a.createElement("span", null, "No knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "baseball",
                            id: "baseball-2",
                            value: "baseball-little",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "baseball-2"
                        }, l.a.createElement("span", null, "A little knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "baseball",
                            id: "baseball-3",
                            value: "baseball-some",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "baseball-3"
                        }, l.a.createElement("span", null, "Some knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "baseball",
                            id: "baseball-4",
                            value: "baseball-lots",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "baseball-4"
                        }, l.a.createElement("span", null, "A lot of knowledge"))), l.a.createElement("div", {
                            className: "demographic-radio"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "baseball",
                            id: "baseball-5",
                            value: "baseball-expert",
                            required: !0
                        }), l.a.createElement("label", {
                            htmlFor: "baseball-5"
                        }, l.a.createElement("span", null, "Expert knowledge"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("input", {
                            type: "submit",
                            className: "standard-button",
                            value: "Next"
                        })))));
                        if (0 == P.indexOf("intro1")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Welcome"), l.a.createElement("div", {
                            className: "instructions"
                        }, "This study is designed to explore how people understand machine learning models. Machine learning models are computer programs that analyze data and learn to make decisions related to the data."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "You do not need to know anything about machine learning models to do this task."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "For this task, you will interact with a machine learning model that has been trained to decide if emails are about hockey or baseball. This model is designed to help a person quickly sort through many emails about hockey and baseball."), l.a.createElement("a", {
                            className: "standard-button",
                            href: "./next"
                        }, "Next"))));
                        if (0 == P.indexOf("intro2")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Task Introduction"), l.a.createElement("div", {
                            className: "instructions"
                        }, "Imagine that your boss has assigned you the tedious job of sorting the thousands of emails in his/her inbox. It's too much for you to do yourself, so you are trying out a machine learning model that is designed to help you finish the job quickly. In this task, you will evaluate the model to see if it is worthwhile to add to your workflow."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "This task has ", l.a.createElement("strong", null, "three phases"), ":"), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, l.a.createElement("strong", null, "Phase 1: Practice")), l.a.createElement("div", {
                            className: "instructions"
                        }, "You will see a practice email to familiarize yourself with the task layout."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, l.a.createElement("strong", null, "Phase 2: Interact")), l.a.createElement("div", {
                            className: "instructions"
                        }, "You will see a series of 20 emails with the model\u2019s decision for each one. Do your best to understand how the model is making its decisions. What kinds of emails does it get right or wrong?"), P.indexOf("explain") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, e)), P.indexOf("instance") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, a)), P.indexOf("feature") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, t)), (P.indexOf("feature") > -1 || P.indexOf("instance") > -1) && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "After you provide feedback for ", l.a.createElement("strong", null, "all"), " 20 emails in this phase, the model will incorporate your feedback for future decisions. It will not incorporate your feedback after each email.")), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, l.a.createElement("strong", null, "Phase 3: Evaluate")), l.a.createElement("div", {
                            className: "instructions"
                        }, "You will evaluate the model. We will ask you about your thoughts on the model and its performance and usefulness, as well as whether you would use this model for your hypothetical job.", P.indexOf("explain") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }))), l.a.createElement("a", {
                            className: "standard-button right-margin",
                            href: "./back"
                        }, "Back"), l.a.createElement("a", {
                            className: "standard-button",
                            href: "./next"
                        }, "Next"))));
                        if (0 == P.indexOf("intro3")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Keep in mind..."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Evaluating the quality of the model is very important in this study. For this reason, we will award at least half of the workers on this task a ", l.a.createElement("strong", null, "bonus of $2"), " based on how thoroughly they evaluate the model's performance and usefulness in the last phase. Try your best!"), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("a", {
                            className: "standard-button right-margin",
                            href: "./back"
                        }, "Back"), l.a.createElement("a", {
                            className: "standard-button",
                            href: "./next"
                        }, "Next"))));
                        if (0 == P.indexOf("pintro")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Phase 1 of 3: Practice"), l.a.createElement("div", {
                            className: "instructions"
                        }, "Let\u2019s do a quick practice! Use the following email to get familiar with the task and to see how the model displays its decision."), P.indexOf("explain") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Remember, the highlighted words are the most important to the model's decision.")), P.indexOf("instance") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Also, please help the model improve by telling it if it\u2019s right or wrong.")), P.indexOf("feature") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Also, please help the model improve by telling it what you consider to be the 3 most important words for deciding if the email is about hockey or baseball.")), l.a.createElement("a", {
                            className: "standard-button",
                            href: "./next"
                        }, "Next"))));
                        if (0 == P.indexOf("instructions")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Phase 2 of 3: Interact with the Model"), l.a.createElement("div", {
                            className: "instructions"
                        }, "You will now be shown a series of 20 emails. For each email, you will also see the decision made by the machine learning model."), P.indexOf("explain") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "You will see three highlighted words that were the most important to the model when deciding if each email is about hockey or baseball.")), P.indexOf("instance") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "To help the model improve in the next phase, you will be asked to give feedback on each decision that it makes. To do this, you will tell the model whether it is right or wrong.")), P.indexOf("feature") > -1 && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "To help the model improve in the next phase, you will be asked to give feedback on each decision that it makes. To do this, you will select the three most important words that you think the model should use to decide if the email is about hockey or baseball.")), l.a.createElement("div", {
                            className: "invis-divider"
                        }), (P.indexOf("feature") > -1 || P.indexOf("instance") > -1) && l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                            className: "instructions"
                        }, "Please note: only after you provide feedback for ", l.a.createElement("strong", null, "all 20 emails"), " in this phase, will the model incorporate that feedback for future decisions.")), l.a.createElement("a", {
                            className: "standard-button",
                            href: "./next"
                        }, "Next"))));
                        if (0 == P.indexOf("practice") || 0 == P.indexOf("train")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, this.state.showInstructions && i, l.a.createElement("div", {className: "email-main-header"}, l.a.createElement("span", {
                            className: "main-header"
                        }, 0 == P.indexOf("practice") ? "Practice Email" : 0 == P.indexOf("train") ? "Email " + j + " of " + I : tt === "same" ? "An email from Phase 2" : "An email similar to one from Phase 2", l.a.createElement("span", {
                            className: "show-instructions-button",
                            onClick: function() {
                                n.setState({
                                    showInstructions: !0
                                })
                            }
                        }, "Show Instructions"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement(f, {
                            pred: x
                        }), P.indexOf("explain") > -1 && l.a.createElement("div", {
                            className: "explain-info"
                        }, "Words highlighted in ", l.a.createElement("span", {
                            className: "explain-info-hl"
                        }, "yellow"), " were most important to the model for making this decision."), l.a.createElement(p, {
                            pred: x,
                            subject: y,
                            sender: w,
                            message: N,
                            explain: P.indexOf("explain") > -1 ? k : [],
                            redraw: this.redraw,
                            feedback: P.indexOf("feature") > -1
                        }), P.indexOf("feature") > -1 && l.a.createElement("div", {
                            className: "explain-info"
                        }, "Please provide feedback ", l.a.createElement("strong", null, "to the model"), " by clicking three words to highlight in ", l.a.createElement("span", {
                            className: "explain-info-hl-blue"
                        }, "blue"), " that ", l.a.createElement("em", null, "you"), " think are most important for deciding the correct category of this email: baseball or hockey."), P.indexOf("feature") > -1 && P.indexOf("explain") > -1 && l.a.createElement("div", {
                            className: "explain-info"
                        }, "You may select any words, including ones that are already highlighted in ", l.a.createElement("span", {
                            className: "explain-info-hl"
                        }, "yellow"), ". If you change your mind on a word, you can click it again to deselect it."), P.indexOf("feature") > -1 && P.indexOf("explain") == -1 && l.a.createElement("div", {
                            className: "explain-info"
                        }, "If you change your mind on a word, you can click it again to deselect it."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), P.indexOf("feature") > -1 && l.a.createElement(b, null), P.indexOf("feature") > -1 && l.a.createElement("div", {
                            className: "invis-divider"
                        }), (P.indexOf("instance") > -1 || P.indexOf("feature") > -1) && l.a.createElement(E, null), (P.indexOf("instance") > -1 || P.indexOf("feature") > -1) && l.a.createElement("div", {
                            className: "invis-divider"
                        }), (P.indexOf("instance") > -1 || P.indexOf("feature") > -1) && P.indexOf("train") > -1 && l.a.createElement("div", {className: "explain-info"}, "Remember, the model will not incorporate your feedback until after you have reviewed all 20 emails."), l.a.createElement("HR"), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement(g, {
                            practice: 0 == P.indexOf("practice"),
                            test: !1,
                            last: j == I
                        }))));
                        if (0 == P.indexOf("tinstructions")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("form", {
                            action: "./next"
                        }, l.a.createElement("h1", null, "Evaluate: Test Emails"), l.a.createElement("div", {
                            className: "instructions"
                        }, (P.indexOf("instance") > -1 || P.indexOf("feature") > -1) && "Reminder: the model has incorporated the feedback that you provided in Phase 2. ", P.indexOf("instance") > -1 || P.indexOf("feature") > -1 && l.a.createElement("div", {
                            className: "invis-divider"
                        }), "The model has made new decisions on a set of 4 emails. For each email, we want to know whether ", l.a.createElement("strong", null, "you"), " think the model will correctly decide if the email is about hockey or baseball."), l.a.createElement("input", {
                            type: "submit",
                            className: "standard-button",
                            value: "Next"
                        })))));
                        if (0 == P.indexOf("openqinstructions")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Phase 3 of 3: Evaluate"), l.a.createElement("form", {
                            action: "./next"
                        }, l.a.createElement("div", {
                            className: "radio-instructions"
                        }, (P.indexOf("feature") > -1 || P.indexOf("instance") > -1) && "The model has incorporated your feedback from the previous phase to update its decision-making about future emails."), l.a.createElement("div", {
                            className: "radio-instructions"
                        }, "Now that you have had time to review the model, we will ask you to evaluate it for the hypothetical task your boss has assigned you."), l.a.createElement("div", {
                            className: "radio-instructions"
                        }, "Remember, you could receive a bonus of $2 based on ", l.a.createElement("strong", null, "how thoroughly you evaluate the model"), "."), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("input", {
                            type: "submit",
                            className: "standard-button",
                            value: "Next"
                        })))));
                        if (0 == P.indexOf("openq3")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Evaluate: Future Performance"), l.a.createElement("div", {
                            className: "instructions"
                        }, l.a.createElement("strong", null, "Please respond to the following questions:")), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("form", {
                            action: "./next"
                        }, l.a.createElement("div", {
                            className: "radio-instructions"
                        }, "15. Do you think the model learned from the 20 emails in the previous phase?"), l.a.createElement("div", {
                            className: "radioset"
                        }, l.a.createElement("span", {
                            className: "radioset-inner"
                        }, l.a.createElement("label", {
                            htmlFor: "q1-1"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-1",
                            value: "learn-1",
                            required: !0
                        }), "Strongly Disagree"), l.a.createElement("label", {
                            htmlFor: "q1-2"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-2",
                            value: "learn-2"
                        }), "Disagree"), l.a.createElement("label", {
                            htmlFor: "q1-3"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-3",
                            value: "learn-3"
                        }), "Slightly Disagree"), l.a.createElement("label", {
                            htmlFor: "q1-4"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-4",
                            value: "learn-4"
                        }), "Neutral"), l.a.createElement("label", {
                            htmlFor: "q1-5"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-5",
                            value: "learn-5"
                        }), "Slightly Agree"), l.a.createElement("label", {
                            htmlFor: "q1-6"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-6",
                            value: "learn-6"
                        }), "Agree"), l.a.createElement("label", {
                            htmlFor: "q1-7"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "learn",
                            id: "q1-7",
                            value: "learn-7"
                        }), "Strongly Agree"))), l.a.createElement("div", {
                            className: "radio-instructions-min"
                        }, "16. Why do you feel that way?"), l.a.createElement("textarea", {
                            className: "openq",
                            rows: 5,
                            id: "learn_why",
                            name: "learn_why",
                            maxlength: "550",
                            required: !0
                        }), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "radio-instructions"
                        }, "17. If the model were now shown another set of emails, how well do you think it would categorize them?"), l.a.createElement("div", {
                            className: "radioset"
                        }, l.a.createElement("span", {
                            className: "radioset-inner"
                        }, l.a.createElement("label", {
                            htmlFor: "q2-1"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-1",
                            value: "perf-1",
                            required: !0
                        }), "Much Worse"), l.a.createElement("label", {
                            htmlFor: "q2-2"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-2",
                            value: "perf-2"
                        }), "Worse"), l.a.createElement("label", {
                            htmlFor: "q2-3"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-3",
                            value: "perf-3"
                        }), "Slightly Worse"), l.a.createElement("label", {
                            htmlFor: "q2-4"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-4",
                            value: "perf-4"
                        }), "About the Same"), l.a.createElement("label", {
                            htmlFor: "q2-5"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-5",
                            value: "perf-5"
                        }), "Slightly Better"), l.a.createElement("label", {
                            htmlFor: "q2-6"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-6",
                            value: "perf-6"
                        }), "Better"), l.a.createElement("label", {
                            htmlFor: "q2-7"
                        }, l.a.createElement("input", {
                            type: "radio",
                            name: "perf",
                            id: "q2-7",
                            value: "perf-7"
                        }), "Much Better"))), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "radio-instructions-min"
                        }, "18. Why do you feel that way?"), l.a.createElement("textarea", {
                            className: "openq",
                            rows: 5,
                            id: "perf_why",
                            name: "perf_why",
                            maxlength: "550",
                            required: !0
                        }), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("input", {
                            type: "submit",
                            className: "standard-button",
                            value: "Next"
                        })))));
                        if (0 == P.indexOf("test")) return l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, this.state.showInstructions && i, l.a.createElement("div", {className: "email-main-header"}, l.a.createElement("span", {
                            className: "main-header"
                        }, tt === "same" ? "An email from Phase 2" : "An email similar to one in Phase 2"), l.a.createElement("span", {
                            className: "show-instructions-button",
                            onClick: function() {
                                n.setState({
                                    showInstructions: !0
                                })
                            }
                        }, "Show Instructions")), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement(f, {
                            pred: x
                        }), l.a.createElement("div", {
                            className: "explain-info"
                        }, "Tell us if you think the model will correctly decide whether this email is about hockey or baseball."), l.a.createElement(p, {
                            pred: "",
                            subject: y,
                            sender: w,
                            message: N,
                            explain: [],
                            redraw: this.redraw,
                            feedback: !1
                        }), l.a.createElement(v, null), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("a", {
                            onClick: function() {
                                var e = "./next?"; - 1 != F[0] && -1 != q[0] && (e += "test_self=" + encodeURIComponent(0 == q[0] ? "hockey" : "baseball"), e += "&test_model=" + encodeURIComponent(0 == F[0] ? "hockey" : "baseball"), location.href = e)
                            },
                            className: "us-button next-email " + (-1 != F[0] && -1 != q[0] ? "ready" : "")
                        }, "Next"))));
                        if (0 == P.indexOf("openq2")) {
                            var r = 4,
                                s = [],
                                o = function(e) {
                                    return function(a) {
                                        var t = a[0],
                                            n = a[1];
                                        s.push(l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                            className: "invis-divider"
                                        }), l.a.createElement("div", {
                                            className: "radio-instructions"
                                        }, r + ". " + n), l.a.createElement("div", {
                                            className: "radioset"
                                        }, l.a.createElement("span", {
                                            className: "radioset-inner"
                                        }, l.a.createElement("label", {
                                            htmlFor: t + "-1"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-1",
                                            value: t + "-1",
                                            required: !0
                                        }), "Strongly Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-2"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-2",
                                            value: t + "-2"
                                        }), "Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-3"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-3",
                                            value: t + "-3"
                                        }), "Slightly Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-4"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-4",
                                            value: t + "-4"
                                        }), "Neutral"), l.a.createElement("label", {
                                            htmlFor: t + "-5"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-5",
                                            value: t + "-5"
                                        }), "Slightly Agree"), l.a.createElement("label", {
                                            htmlFor: t + "-6"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-6",
                                            value: t + "-6"
                                        }), "Agree"), l.a.createElement("label", {
                                            htmlFor: t + "-7"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-7",
                                            value: t + "-7"
                                        }), "Strongly Agree"))))), e && s.push(l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                            className: "radio-instructions-min"
                                        }, ++r + ". Why do you feel that way?"), l.a.createElement("textarea", {
                                            className: "openq",
                                            rows: 5,
                                            id: t + "_why",
                                            name: t + "_why",
                                            maxlength: "550",
                                            required: !0
                                        }))), r += 1
                                    }
                                };
                            return s.push(l.a.createElement("div", {
                                className: "radio-instructions"
                            }, l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("strong", null, "Remember that you are evaluating this model to help you quickly sort your boss's sport-related emails. Please keep this scenario in mind as you respond to these hypothetical situations:"))), [
                                ["accuracy_standard", "It is acceptable for the model to make a few mistakes."],
                                ["frustration", "I would feel frustrated if I were to use this model to automatically sort my boss's emails."],
                                ["trust", "I would trust this model to correctly categorize my boss's emails that are about hockey or baseball."],
                                ["recommend", "I would use this model to help me sort my boss's emails."],
                                ["fdbk_importance", "If I were to use this model, it would be important to have the ability to provide feedback to improve it."]
                            ].forEach(o(!0)), l.a.createElement("div", {
                                className: "wrap"
                            }, l.a.createElement("div", {
                                className: "inner-wrap"
                            }, l.a.createElement("div", {
                                className: "inner-wrap2"
                            }, l.a.createElement("h1", null, "Evaluate: Hypothetical Scenarios"), l.a.createElement("form", {
                                action: "./next"
                            }, s, l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "radio-instructions"
                            }, r + ". Overall, how did you feel about the experience of using this model?"), l.a.createElement("textarea", {
                                className: "openq",
                                rows: 5,
                                id: "overall",
                                name: "overall",
                                maxlength: "550",
                                required: !0
                            }), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("input", {
                                type: "submit",
                                className: "standard-button",
                                value: "Next"
                            })))))
                        }
                        if (0 == P.indexOf("openq")) {
                            var r = 2,
                                s = [],
                                o = function(e) {
                                    return function(a) {
                                        var t = a[0],
                                            n = a[1];
                                        s.push(l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                            className: "invis-divider"
                                        }), l.a.createElement("div", {
                                            className: "radio-instructions"
                                        }, r + ". " + n), l.a.createElement("div", {
                                            className: "radioset"
                                        }, l.a.createElement("span", {
                                            className: "radioset-inner"
                                        }, l.a.createElement("label", {
                                            htmlFor: t + "-1"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-1",
                                            value: t + "-1",
                                            required: !0
                                        }), "Strongly Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-2"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-2",
                                            value: t + "-2"
                                        }), "Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-3"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-3",
                                            value: t + "-3"
                                        }), "Slightly Disagree"), l.a.createElement("label", {
                                            htmlFor: t + "-4"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-4",
                                            value: t + "-4"
                                        }), "Neutral"), l.a.createElement("label", {
                                            htmlFor: t + "-5"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-5",
                                            value: t + "-5"
                                        }), "Slightly Agree"), l.a.createElement("label", {
                                            htmlFor: t + "-6"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-6",
                                            value: t + "-6"
                                        }), "Agree"), l.a.createElement("label", {
                                            htmlFor: t + "-7"
                                        }, l.a.createElement("input", {
                                            type: "radio",
                                            name: t,
                                            id: t + "-7",
                                            value: t + "-7"
                                        }), "Strongly Agree"))))), e && s.push(l.a.createElement(l.a.Fragment, null, l.a.createElement("div", {
                                            className: "radio-instructions-min"
                                        }, ++r + ". Why do you feel that way?"), l.a.createElement("textarea", {
                                            className: "openq",
                                            rows: 5,
                                            id: t + "_why",
                                            name: t + "_why",
                                            maxlength: "550",
                                            required: !0
                                        }))), r += 1
                                    }
                                };
                            return [
                                ["perceived_accuracy", "The model is able to distinguish between hockey and baseball emails."],
                                ["understanding", "I understand how this model makes decisions."]
                            ].forEach(o(!1)), l.a.createElement("div", {
                                className: "wrap"
                            }, l.a.createElement("div", {
                                className: "inner-wrap"
                            }, l.a.createElement("div", {
                                className: "inner-wrap2"
                            }, l.a.createElement("h1", null, "Evaluate: Model Performance"), l.a.createElement("form", {
                                action: "./next"
                            }, l.a.createElement("div", {
                                className: "radio-instructions"
                            }, l.a.createElement("strong", null, "Please respond to the following prompts:")), l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("div", {
                                className: "radio-instructions"
                            }, "1. Describe in your own words how the machine learning model decided whether emails were about baseball or hockey."), l.a.createElement("textarea", {
                                className: "openq",
                                rows: 5,
                                id: "how_decide",
                                name: "how_decide",
                                maxlength: "550",
                                required: !0
                            }), s, l.a.createElement("div", {
                                className: "invis-divider"
                            }), l.a.createElement("input", {
                                type: "submit",
                                className: "standard-button",
                                value: "Next"
                            })))))
                        }
                        return 0 == P.indexOf("complete") ? l.a.createElement("div", {
                            className: "wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap"
                        }, l.a.createElement("div", {
                            className: "inner-wrap2"
                        }, l.a.createElement("h1", null, "Task Complete"), l.a.createElement("div", {
                            className: "instructions"
                        }, "Congratulations, you are now finished with this study. "), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Based on your evaluation, you will receive a full $2 bonus. Great job!"), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "instructions"
                        }, "Please ", l.a.createElement("strong", null, "submit the following code on Mechanical Turk"), " for your payment (you can copy and paste it):"), l.a.createElement("div", {
                            className: "invis-divider"
                        }), l.a.createElement("div", {
                            className: "final-code"
                        }, S)))) : (alert("Failed to detect mode: " + P), l.a.createElement(l.a.Fragment, null))
                    }
                }]), a
            }(n.Component);
            Boolean("localhost" === window.location.hostname || "[::1]" === window.location.hostname || window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));
            r.a.render(l.a.createElement(Y, null), document.getElementById("root")), "serviceWorker" in navigator && navigator.serviceWorker.ready.then(function(e) {
                e.unregister()
            })
        },
        9: function(e, a, t) {
            e.exports = t(19)
        }
    },
    [
        [9, 2, 1]
    ]
]);
//# sourceMappingURL=main.2bcbbf7a.chunk.js.map