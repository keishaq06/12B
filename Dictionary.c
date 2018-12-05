/*Keisha Quirimit
 * kquirimi
 * CMPS12M-02
 * March 16, 2018
 * Dictionary ADT using hash table
 * Dictionary.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "Dictionary.h"

const int tableSize = 101;

// --------Hash function -----------------------------

// rotate the bits in an unsigned int
unsigned int rotate_left(unsigned int value, int shift) {
	int sizeInBits = 8 * sizeof(unsigned int);
	shift = shift & (sizeInBits - 1);
	if (shift == 0)
		return value;
	return (value << shift) | (value >> (sizeInBits - shift));
}

// pre_hash()
// turn a string into an unsigned int
unsigned int pre_hash(char* input) {
	unsigned int result = 0xBAE86554;
	while (*input) {
		result ^= *input++;
		result = rotate_left(result, 5);
	}
	return result;
}

// hash()
// turns a string into an int in the range 0 to tableSize-1
int hash(char* key) {
	return pre_hash(key) % tableSize;
}

//-------Node ---------------------------------

typedef struct NodeObj {
	char* key;
	char* value;
	struct NodeObj* next;
} NodeObj;

typedef NodeObj* Node;

Node newNode(char* k, char* val) {
	Node N = malloc(sizeof(NodeObj));
	assert(N!=NULL);
	N->key = k;
	N->value = val;
	N->next = NULL;
	return (N);
}

//destructor for Node
void freeNode(Node* pN) {
	if (pN != NULL && *pN != NULL) {
		free(*pN);
		*pN = NULL;
	}
}

//-------List --------------------------------

typedef struct ListObj {
	Node head;
	int numItems;
} ListObj;

typedef ListObj* List;

List newList(void) {
	List L = calloc(tableSize, sizeof(ListObj));
	assert(L!=NULL);
	L->head = NULL;
	L->numItems = 0;
	return L;
}

//destructor for List
void freeList(List* pL) {
	if (pL != NULL && *pL != NULL) {
		free(*pL);
		*pL = NULL;
	}
}

//prints String representation of List
void printList(FILE* out, List L) {
	Node N = L->head;
	int i;

	if (L == NULL) {
		fprintf(stderr,
				"List Error: calling printList() on NULL List reference\n");
		exit(EXIT_FAILURE);
	}

	for (i = 0; i < L->numItems; i++) {
		fprintf(out, "%s %s\n", N->key, N->value);
		N = N->next;
	}
}

//---------Dictionary --------------------------

typedef struct DictionaryObj {
	List T[101];
	int numItems;
} DictionaryObj;

typedef DictionaryObj* Dictionary;

Dictionary newDictionary(void) {
	Dictionary D = malloc(sizeof(DictionaryObj));
	assert(D!=NULL);
	D->numItems = 0;
	//fills array with Lists
	for (int i = 0; i < tableSize; i++) {
		D->T[i] = newList();
	}
	return D;
}

//destructor for Dictionary type
void freeDictionary(Dictionary* pD) {
	if (pD != NULL && *pD != NULL) {
		if (!isEmpty(*pD))
			makeEmpty(*pD);
		free(*pD);
		*pD = NULL;
	}
}

//------findKey() function--------------------------------

//returns a reference to the Node with the specified key
Node findKey(Dictionary D, char* k) {
	Node N = NULL;
	List L = NULL;
	int i, j;
	int listSize;
	for (i = 0; i < tableSize; i++) { //traverse through hash table
		L = D->T[i];
		N = L->head;
		listSize = L->numItems;
		for (j = 0; j < listSize; j++) { //traverse through LinkedList at T[i]
			if ((strcmp(N->key, k) == 0) == 1) { //if found Node with specified key
				return N;
			}
			N = N->next;
		}
	}
	return N; //returns NULL if Node with key not found
}
//---------------------------------------------------

//returns 1 if Dictionary is empty and 0 if it isn't empty
int isEmpty(Dictionary D) {
	if (D == NULL) {
		fprintf(stderr,
				"Dictionary Error: calling isEmpty() on NULL Dictionary reference\n");
		exit(EXIT_FAILURE);
	}
	return (D->numItems == 0);
}

//returns number of (key,value) pairs in the Dictionary
int size(Dictionary D) {
	return D->numItems;
}

// returns the value v such that (k, v) is in D, or returns NULL if no
// such value v exists.
char* lookup(Dictionary D, char* k) {
	Node N = findKey(D, k);
	if (N == NULL)
		return NULL;
	else
		return N->value;
}

// inserts new (key,value) pair into D
// pre: lookup(D, k)==NULL
void insert(Dictionary D, char* k, char* v) {
	List L = NULL;

	if (findKey(D, k) != NULL) {
		fprintf(stderr, "Dictionary Error: cannot insert a duplicate key");
		exit(EXIT_FAILURE);
	} else {
		Node add = newNode(k, v);
		int index = hash(k);
		L = D->T[index];
		add->next = L->head;
		L->head = add;
		L->numItems++;
		D->numItems++;
	}
}

// deletes pair with the key k
// pre: lookup(D, k)!=NULL
void delete(Dictionary D, char* k) {
	Node target = findKey(D, k);
	int index = hash(k);
	Node N = D->T[index]->head;

	if (D == NULL) {
		fprintf(stderr,
				"Dictionary Error: calling delete() on a NULL Dictionary reference\n");
		exit(EXIT_FAILURE);
	} else if (target == NULL) {
		fprintf(stderr, "cannot delete non-existent key");
		exit(EXIT_FAILURE);
	} else if (D->T[index]->numItems == 1) { //if deleting first element in a LinkedList
		D->T[index]->head = target->next;
		D->T[index]->numItems--;
		freeNode(&N);
	} else {
		while ((strcmp(N->next->key, k)) != 0) { //find Node preceding target
			N = N->next;
		}
		N->next = target->next;
		D->T[index]->numItems--;
	}
	D->numItems--;
}

// re-sets D to the empty state.
// pre: none
void makeEmpty(Dictionary D) {
	Node N = NULL;
	List L = NULL;
	int i, j;

	if (D == NULL) {
		fprintf(stderr,
				"Dictionary Error: calling makeEmpty() on a NULL Dictionary reference\n");
		exit(EXIT_FAILURE);
	} else if (D->numItems == 0) {
		fprintf(stderr,
				"Dictionary Error: calling makeEmpty() on an empty Dictionary\n");
		exit(EXIT_FAILURE);
	} else {
		for (i = 0; i < tableSize; i++) { //traverse through hash table
			L = D->T[i];
			for (j = 0; j < L->numItems; j++) { //traverse through LinkedList at T[i]
				N = L->head;
				L->head = N->next;
				L->numItems--;
				D->numItems--;
				freeNode(&N);
			}
			freeList(&L);
		}
	}
}

// pre: none
// prints a text representation of D to the file pointed to by out
void printDictionary(FILE* out, Dictionary D) {
	List L = NULL;
	int i;

	if (D == NULL) {
		fprintf(stderr,
				"Dictionary Error: calling printDictionary() on NULL Dictionary reference\n");
		exit(EXIT_FAILURE);
	}
	for (i = 0; i < tableSize; i++) { //traverse through hash table
		L = D->T[i];
		if (L->numItems > 0)
			printList(out, L); //print each List
	}

}

