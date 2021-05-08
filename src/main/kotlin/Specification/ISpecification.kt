package Specification

interface ISpecification<T> {
    fun isSatisfiedBy(candidate: T): Boolean;
    fun and(other: ISpecification<T>): ISpecification<T>;
    fun or(other: ISpecification<T>): ISpecification<T>;
    fun not(): ISpecification<T>;
}

abstract class CompositeSpecification<T> : ISpecification<T> {
    override fun and(other: ISpecification<T>): ISpecification<T> =
            AndSpecification(this, other);

    override fun or(other: ISpecification<T>): ISpecification<T> =
            OrSpecification(this, other);

    override fun not(): ISpecification<T> =
            NotSpecification(this);
}

class AndSpecification<T>(_one: ISpecification<T>, _other: ISpecification<T>) : CompositeSpecification<T>() {
    private val one = _one;
    private val other = _other;

    override fun isSatisfiedBy(candidate: T) =
            one.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
}

class OrSpecification<T>(_one: ISpecification<T>, _other: ISpecification<T>) : CompositeSpecification<T>() {
    private val one = _one;
    private val other = _other;

    override fun isSatisfiedBy(candidate: T) =
            one.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
}

class NotSpecification<T>(_wrapped: ISpecification<T>) : CompositeSpecification<T>() {
    private val wrapped = _wrapped;

    override fun isSatisfiedBy(candidate: T) =
            !wrapped.isSatisfiedBy(candidate);
}