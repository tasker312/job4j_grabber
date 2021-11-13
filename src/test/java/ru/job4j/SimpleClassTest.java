package ru.job4j;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleClassTest {

    @Test
    public void test() {
        Assert.assertEquals("test", new SimpleClass().test());
    }
}
