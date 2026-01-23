# Codebase Architecture

## Overview

The codebase has been restructured to follow a modular, independent architecture where features are separated and do not import from each other.

## Package Structure

```
edu.brandeis.cosi103a.ip1/
├── core/                          # Core domain models and interfaces
│   ├── Card.java                  # Base interface for all cards
│   ├── AutomationCard.java        # Abstract base for automation cards
│   ├── CryptocurrencyCard.java    # Abstract base for crypto cards
│   ├── Player.java                # Player model
│   ├── Deck.java                  # Deck management
│   ├── Supply.java                # Card supply management
│   └── GameState.java             # Game state management
│
├── features/                       # Feature modules (independent)
│   ├── cards/                     # Card implementations
│   │   ├── automation/            # Automation card feature
│   │   │   ├── Method.java
│   │   │   ├── Module.java
│   │   │   └── Framework.java
│   │   └── cryptocurrency/        # Cryptocurrency card feature
│   │       ├── Bitcoin.java
│   │       ├── Ethereum.java
│   │       └── Dogecoin.java
│   │
│   └── phases/                    # Game phase implementations
│       ├── BuyPhase.java          # Buy phase logic
│       └── CleanupPhase.java      # Cleanup phase logic
│
├── game/                          # Game orchestration (depends on core + features)
│   ├── Game.java                  # Main game loop
│   └── GameOrchestrator.java      # Game entry point
│
└── utils/                         # Shared utilities (no domain logic)
    ├── cli.java
    └── tools.java
```

## Dependency Rules

### ✅ Allowed Dependencies

1. **Features → Core**: Features can import from `core/` package only
   - ✅ `Method` extends `AutomationCard` from core
   - ✅ `BuyPhase` uses `Player` and `Supply` from core

2. **Game → Core + Features**: Game orchestration can import from both
   - ✅ `Game` imports `Card`, `Player` from core
   - ✅ `Game` imports `BuyPhase`, `CleanupPhase` from features

3. **Utils → Nothing**: Utils should be pure utility functions
   - ✅ No imports from core, features, or game
   - ✅ Only standard library imports

### ❌ Forbidden Dependencies

1. **Features → Features**: Features cannot import from other features
   - ❌ `BuyPhase` cannot import `Method` directly
   - ✅ Use interfaces from `core/` instead

2. **Core → Features**: Core cannot depend on features
   - Core defines contracts, features implement them

3. **Features → Game**: Features cannot import from game
   - Game orchestrates features, not the other way around

## Verification

All imports have been verified to follow these rules:

- ✅ Card features only import from `core`
- ✅ Phase features only import from `core`
- ✅ Game imports from both `core` and `features`
- ✅ No cross-feature imports
- ✅ Utils have no project imports

## Benefits

1. **Independence**: Features can be developed/tested independently
2. **Testability**: Easy to mock core interfaces for feature testing
3. **Extensibility**: New card types can be added without modifying existing features
4. **Maintainability**: Clear boundaries make code easier to understand
5. **Reusability**: Core interfaces can be reused across different game implementations

## Migration Summary

- ✅ Created `core/` package with base interfaces and models
- ✅ Restructured card classes into independent feature packages
- ✅ Updated phase classes to use core interfaces
- ✅ Created game orchestration module
- ✅ Fixed syntax errors (multiple public classes in one file)
- ✅ Verified all dependencies follow the rules
