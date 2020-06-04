package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.FilterCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

/** Protected */ class MessageCriteriaConsumer implements Consumer<FilterCriteria> {
    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root<Message> root;

    public MessageCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<Message> root) {
        this.predicate = predicate;
        this.builder = builder;
        this.root = root;
    }

    @Override
    public void accept(FilterCriteria param) {
        switch (param.getField()) {
            case "receiver": receiver(param.getValue()); break;
            case "sender": sender(param.getValue()); break;
            case "project": project(param.getValue()); break;
            case "unread": unread(); break;
            case "accepted": accepted(param.getValue()); break;
        }
    }

    public void receiver(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("receiver"), value));
    }

    public void sender(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("sender"), value));
    }

    public void project(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("project"), value));
    }

    public void unread() {
        predicate = builder.and(predicate, builder.isNull(root.get("accepted")));
    }

    public void accepted(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("accepted"), value));
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
